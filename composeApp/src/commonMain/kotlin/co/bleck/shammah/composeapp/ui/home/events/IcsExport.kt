@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.events

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

// Multiplatform iCalendar builder — replaces the java.util.Calendar/SimpleDateFormat
// implementation that previously lived in the Android-only EventsScreen.

private fun Int.pad2() = toString().padStart(2, '0')
private fun Int.pad4() = toString().padStart(4, '0')

/** yyyyMMdd'T'HHmmss'Z' in UTC */
private fun Instant.toIcsUtcStamp(): String {
    val dt = toLocalDateTime(TimeZone.UTC)
    return "${dt.year.pad4()}${dt.month.number.pad2()}${dt.day.pad2()}" +
        "T${dt.hour.pad2()}${dt.minute.pad2()}${dt.second.pad2()}Z"
}

/** yyyyMMdd */
private fun LocalDate.toIcsDate(): String =
    "${year.pad4()}${month.number.pad2()}${day.pad2()}"

private fun escapeIcsValue(value: String): String =
    value.replace("\\", "\\\\")
        .replace(";", "\\;")
        .replace(",", "\\,")
        .replace("\n", "\\n")

private val timePattern = Regex("(\\d{1,2}):(\\d{2})\\s*(AM|PM)?")

/** Parses formats like "12:30 PM", "12:30PM", "18:00". */
private fun parseEventTime(raw: String): LocalTime? {
    val match = timePattern.find(raw.trim().uppercase()) ?: return null
    var hour = match.groupValues[1].toIntOrNull() ?: return null
    val minute = match.groupValues[2].toIntOrNull() ?: return null
    when (match.groupValues[3]) {
        "PM" -> if (hour < 12) hour += 12
        "AM" -> if (hour == 12) hour = 0
    }
    if (hour !in 0..23 || minute !in 0..59) return null
    return LocalTime(hour, minute)
}

fun buildEventsIcs(
    events: List<Event>,
    today: LocalDate,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String {
    val builder = StringBuilder()
    builder.append("BEGIN:VCALENDAR\r\n")
    builder.append("VERSION:2.0\r\n")
    builder.append("PRODID:-//BleckOps//Shammah//ES\r\n")
    builder.append("CALSCALE:GREGORIAN\r\n")
    builder.append("METHOD:PUBLISH\r\n")

    for (event in events) {
        val eventDate = event.date.toLocalDateTime(timeZone).date

        builder.append("BEGIN:VEVENT\r\n")
        builder.append("UID:${event.id.ifBlank { "shammah-${event.hashCode()}" }}\r\n")
        builder.append("DTSTAMP:${event.createdAt.toIcsUtcStamp()}\r\n")

        if (event.type == EventType.birthdays) {
            // Birthdays match by month+day and recur every year.
            val start = LocalDateTime(
                LocalDate(today.year, eventDate.month, eventDate.day),
                LocalTime(9, 0),
            ).toInstant(timeZone)
            builder.append("DTSTART:${start.toIcsUtcStamp()}\r\n")
            builder.append("DTEND:${(start + 1.hours).toIcsUtcStamp()}\r\n")
            builder.append("RRULE:FREQ=YEARLY\r\n")
        } else {
            val parsedTime = parseEventTime(event.time)
            if (parsedTime != null) {
                val start = LocalDateTime(eventDate, parsedTime).toInstant(timeZone)
                builder.append("DTSTART:${start.toIcsUtcStamp()}\r\n")
                // Default duration: 2 hours
                builder.append("DTEND:${(start + 2.hours).toIcsUtcStamp()}\r\n")
            } else {
                // All-day event
                val nextDay = (event.date + 1.days).toLocalDateTime(timeZone).date
                builder.append("DTSTART;VALUE=DATE:${eventDate.toIcsDate()}\r\n")
                builder.append("DTEND;VALUE=DATE:${nextDay.toIcsDate()}\r\n")
            }
        }

        builder.append("SUMMARY:${escapeIcsValue(event.title)}\r\n")
        if (event.description.isNotBlank()) {
            builder.append("DESCRIPTION:${escapeIcsValue(event.description)}\r\n")
        }
        if (event.location.isNotBlank()) {
            builder.append("LOCATION:${escapeIcsValue(event.location)}\r\n")
        }
        builder.append("END:VEVENT\r\n")
    }
    builder.append("END:VCALENDAR\r\n")
    return builder.toString()
}
