@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.components

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

// Multiplatform replacement for java.text/java.time Spanish locale formatting.

private val monthsFullEs = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
)

private val monthsShortEs = listOf(
    "ene", "feb", "mar", "abr", "may", "jun",
    "jul", "ago", "sep", "oct", "nov", "dic"
)

val Month.fullNameEs: String get() = monthsFullEs[number - 1]
val Month.shortNameEs: String get() = monthsShortEs[number - 1]

val DayOfWeek.narrowNameEs: String
    get() = when (this) {
        DayOfWeek.MONDAY    -> "L"
        DayOfWeek.TUESDAY   -> "M"
        DayOfWeek.WEDNESDAY -> "M"
        DayOfWeek.THURSDAY  -> "J"
        DayOfWeek.FRIDAY    -> "V"
        DayOfWeek.SATURDAY  -> "S"
        DayOfWeek.SUNDAY    -> "D"
    }

/** "4 Ago" */
fun LocalDate.shortDisplayEs(): String =
    "$day ${month.shortNameEs.replaceFirstChar { it.uppercaseChar() }}"

/** "04 ago 2026" */
fun Instant.toShortDateEs(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val date = toLocalDateTime(timeZone).date
    val dd = date.day.toString().padStart(2, '0')
    return "$dd ${date.month.shortNameEs} ${date.year}"
}

/** "04 de agosto de 2026" */
fun Instant.toLongDateEs(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val date = toLocalDateTime(timeZone).date
    val dd = date.day.toString().padStart(2, '0')
    return "$dd de ${date.month.fullNameEs} de ${date.year}"
}
