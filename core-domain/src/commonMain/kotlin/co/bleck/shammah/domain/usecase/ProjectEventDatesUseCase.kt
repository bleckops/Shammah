@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.matchesDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Month

class ProjectEventDatesUseCase(
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    operator fun invoke(
        events: List<Event>,
        today: LocalDate = LocalDate(1970, 1, 1),
        yearRange: IntRange = -1..2,
    ): Map<LocalDate, List<Event>> {
        val result = mutableMapOf<LocalDate, MutableList<Event>>()
        events.forEach { event ->
            val rawDate = event.date.toLocalDateTime(timeZone).date
            for (year in (today.year + yearRange.first)..(today.year + yearRange.last)) {
                var candidate = LocalDate(year, 1, 1)
                val end = LocalDate(year, 12, 31)
                while (candidate <= end) {
                    if (event.matchesDate(candidate, rawDate)) {
                        result.getOrPut(candidate) { mutableListOf() }.add(event)
                    }
                    candidate = nextDay(candidate)
                }
            }
        }
        return result
    }

    private fun nextDay(date: LocalDate): LocalDate {
        val monthLength = when (date.month) {
            Month.FEBRUARY -> if (date.year % 4 == 0 && (date.year % 100 != 0 || date.year % 400 == 0)) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
        return if (date.day < monthLength) LocalDate(date.year, date.month, date.day + 1)
        else if (date.month == Month.DECEMBER) LocalDate(date.year + 1, Month.JANUARY, 1)
        else LocalDate(date.year, Month.entries[date.month.ordinal + 1], 1)
    }
}
