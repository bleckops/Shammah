package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class ProjectEventDatesUseCaseTest {

    private val useCase = ProjectEventDatesUseCase()
    private val today = LocalDate.of(2025, 6, 15)

    private fun dateAt(year: Int, month: Int, day: Int): Date =
        Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant())

    @Test
    fun `groups regular events by exact date`() {
        val eventDate = LocalDate.of(2025, 7, 1)
        val events = listOf(
            Event(id = "e1", title = "Camp", date = dateAt(2025, 7, 1))
        )

        val result = useCase(events, today = today)

        assertEquals(1, result[eventDate]?.size)
        assertEquals("e1", result[eventDate]?.first()?.id)
    }

    @Test
    fun `projects birthdays across year range`() {
        val events = listOf(
            Event(id = "b1", title = "Birthday", type = EventType.birthdays, date = dateAt(1990, 6, 15))
        )

        val result = useCase(events, today = today, yearRange = -1..2)

        assertEquals(4, result.size)
        assertTrue(result.containsKey(LocalDate.of(2024, 6, 15)))
        assertTrue(result.containsKey(LocalDate.of(2025, 6, 15)))
        assertTrue(result.containsKey(LocalDate.of(2026, 6, 15)))
        assertTrue(result.containsKey(LocalDate.of(2027, 6, 15)))
    }
}
