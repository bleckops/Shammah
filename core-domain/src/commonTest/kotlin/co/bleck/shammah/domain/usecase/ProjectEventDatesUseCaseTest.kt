@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectEventDatesUseCaseTest {
    private val useCase = ProjectEventDatesUseCase(TimeZone.UTC)
    private val today = LocalDate(2025, 6, 15)

    private fun dateAt(year: Int, month: Int, day: Int) =
        LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC)

    @Test
    fun groupsRegularEventsByExactDate() {
        val eventDate = LocalDate(2025, 7, 1)
        val events = listOf(Event(id = "e1", title = "Camp", date = dateAt(2025, 7, 1)))

        val result = useCase(events, today = today)
        assertEquals(1, result[eventDate]?.size)
    }

    @Test
    fun projectsBirthdaysAcrossYearRange() {
        val events = listOf(
            Event(id = "b1", title = "Birthday", type = EventType.birthdays, date = dateAt(1990, 6, 15)),
        )

        val result = useCase(events, today = today, yearRange = -1..2)
        assertEquals(4, result.size)
        assertTrue(result.containsKey(LocalDate(2024, 6, 15)))
        assertTrue(result.containsKey(LocalDate(2025, 6, 15)))
        assertTrue(result.containsKey(LocalDate(2026, 6, 15)))
        assertTrue(result.containsKey(LocalDate(2027, 6, 15)))
    }
}
