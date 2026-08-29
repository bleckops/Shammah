package co.bleck.shammah.domain.model

import kotlinx.datetime.LocalDate

const val SINGLE_REPEAT = "single"
const val RECURSIVE_REPEAT = "recursive"
const val DAILY_PERIOD = "daily"
const val WEEKLY_PERIOD = "weekly"
const val MONTHLY_PERIOD = "monthly"
const val YEARLY_PERIOD = "yearly"

private val validPeriods = setOf(DAILY_PERIOD, WEEKLY_PERIOD, MONTHLY_PERIOD, YEARLY_PERIOD)

/** Returns whether [date] is one of this event's calendar occurrences. */
fun Event.matchesDate(date: LocalDate, startDate: LocalDate): Boolean {
    val repeatType = repeat ?: if (type == EventType.birthdays) RECURSIVE_REPEAT else SINGLE_REPEAT
    if (repeatType != RECURSIVE_REPEAT) return date == startDate

    val recurrencePeriod = (period ?: if (type == EventType.birthdays) YEARLY_PERIOD else "").lowercase()
    if (recurrencePeriod !in validPeriods || date < startDate) return false
    if (type == EventType.birthdays && recurrencePeriod == YEARLY_PERIOD && repeatNumber == null) {
        return date.month == startDate.month && date.day == startDate.day
    }

    val occurrence = when (recurrencePeriod) {
        DAILY_PERIOD -> daysBetween(startDate, date)
        WEEKLY_PERIOD -> daysBetween(startDate, date).takeIf { it % 7 == 0 }?.div(7) ?: return false
        MONTHLY_PERIOD -> monthDistance(startDate, date).takeIf { it >= 0 } ?: return false
        YEARLY_PERIOD -> (date.year - startDate.year).takeIf { it >= 0 } ?: return false
        else -> return false
    }
    if (repeatNumber != null && (repeatNumber <= 0 || occurrence >= repeatNumber)) return false
    return when (recurrencePeriod) {
        DAILY_PERIOD, WEEKLY_PERIOD -> true
        MONTHLY_PERIOD -> date == startDate.plusMonthsClamped(occurrence)
        YEARLY_PERIOD -> date == startDate.plusYearsClamped(occurrence)
        else -> false
    }
}

fun Event.recurrenceSummary(): String? {
    val repeatType = repeat ?: if (type == EventType.birthdays) RECURSIVE_REPEAT else SINGLE_REPEAT
    if (repeatType != RECURSIVE_REPEAT) return null
    val periodLabel = when ((period ?: if (type == EventType.birthdays) YEARLY_PERIOD else "").lowercase()) {
        DAILY_PERIOD -> "Every day"
        WEEKLY_PERIOD -> "Every week"
        MONTHLY_PERIOD -> "Every month"
        YEARLY_PERIOD -> "Yearly"
        else -> return null
    }
    return "$periodLabel · ${repeatNumber?.let { "$it occurrences" } ?: "Unlimited"}"
}

private fun monthDistance(from: LocalDate, to: LocalDate): Int =
    (to.year - from.year) * 12 + to.month.ordinal - from.month.ordinal

// Gregorian calendar day number, avoiding timezone/millisecond arithmetic.
private fun daysBetween(from: LocalDate, to: LocalDate): Int = civilDayNumber(to) - civilDayNumber(from)

private fun civilDayNumber(date: LocalDate): Int {
    val adjustedYear = date.year - if (date.month.ordinal <= 1) 1 else 0
    val era = adjustedYear.floorDiv(400)
    val yearOfEra = adjustedYear - era * 400
    val month = date.month.ordinal + if (date.month.ordinal > 1) -2 else 10
    val dayOfYear = (153 * month + 2) / 5 + date.day - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    return era * 146097 + dayOfEra
}

private fun LocalDate.plusMonthsClamped(months: Int): LocalDate {
    val absoluteMonth = year * 12 + month.ordinal + months
    val targetYear = absoluteMonth.floorDiv(12)
    val targetMonth = kotlinx.datetime.Month.entries[absoluteMonth.mod(12)]
    return LocalDate(targetYear, targetMonth, day.coerceAtMost(daysInMonth(targetYear, targetMonth)))
}

private fun LocalDate.plusYearsClamped(years: Int): LocalDate {
    val targetYear = year + years
    return LocalDate(targetYear, month, day.coerceAtMost(daysInMonth(targetYear, month)))
}

private fun daysInMonth(year: Int, month: kotlinx.datetime.Month): Int = when (month) {
    kotlinx.datetime.Month.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    kotlinx.datetime.Month.APRIL, kotlinx.datetime.Month.JUNE,
    kotlinx.datetime.Month.SEPTEMBER, kotlinx.datetime.Month.NOVEMBER -> 30
    else -> 31
}
