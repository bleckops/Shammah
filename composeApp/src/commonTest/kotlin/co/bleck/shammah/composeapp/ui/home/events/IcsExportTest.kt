@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.events

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class IcsExportTest {
    private val timeZone = TimeZone.UTC

    private fun dateAt(year: Int, month: Int, day: Int) =
        LocalDate(year, month, day).atStartOfDayIn(timeZone)

    @Test
    fun buildsTimedEventWithTitleAndLocation() {
        val events = listOf(
            Event(
                id = "e1",
                title = "Camp; Night",
                description = "Line1\nLine2",
                date = dateAt(2025, 8, 1),
                time = "18:00",
                location = "Church, Hall A",
            ),
        )

        val ics = buildEventsIcs(events, today = LocalDate(2025, 8, 1), timeZone = timeZone)

        assertTrue(ics.startsWith("BEGIN:VCALENDAR"))
        assertContains(ics, "UID:e1")
        assertContains(ics, "SUMMARY:Camp\\; Night")
        assertContains(ics, "DESCRIPTION:Line1\\nLine2")
        assertContains(ics, "LOCATION:Church\\, Hall A")
        assertContains(ics, "DTSTART:")
        assertContains(ics, "END:VCALENDAR")
    }

    @Test
    fun buildsBirthdayWithYearlyRrule() {
        val events = listOf(
            Event(
                id = "b1",
                title = "Birthday",
                type = EventType.birthdays,
                date = dateAt(1990, 3, 10),
            ),
        )

        val ics = buildEventsIcs(events, today = LocalDate(2025, 1, 1), timeZone = timeZone)

        assertContains(ics, "RRULE:FREQ=YEARLY")
        assertContains(ics, "SUMMARY:Birthday")
    }

    @Test
    fun buildsAllDayEventWhenTimeMissing() {
        val events = listOf(
            Event(
                id = "e2",
                title = "All day",
                date = dateAt(2025, 9, 5),
                time = "",
            ),
        )

        val ics = buildEventsIcs(events, today = LocalDate(2025, 9, 5), timeZone = timeZone)

        assertContains(ics, "DTSTART;VALUE=DATE:20250905")
        assertContains(ics, "DTEND;VALUE=DATE:")
    }
}
