package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class FilterEventsForDateUseCaseTest {

    private val useCase = FilterEventsForDateUseCase()

    private fun dateAt(year: Int, month: Int, day: Int): Date =
        Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant())

    @Test
    fun `returns events on exact date`() {
        val target = LocalDate.of(2025, 6, 15)
        val events = listOf(
            Event(id = "e1", title = "Match", date = dateAt(2025, 6, 15)),
            Event(id = "e2", title = "Other", date = dateAt(2025, 6, 16))
        )

        val result = useCase(events, target)

        assertEquals(1, result.size)
        assertEquals("e1", result[0].id)
    }

    @Test
    fun `birthday matches by month and day ignoring year`() {
        val target = LocalDate.of(2025, 3, 10)
        val events = listOf(
            Event(id = "b1", title = "Birthday", type = EventType.birthdays, date = dateAt(1990, 3, 10)),
            Event(id = "b2", title = "Wrong day", type = EventType.birthdays, date = dateAt(1990, 3, 11))
        )

        val result = useCase(events, target)

        assertEquals(1, result.size)
        assertEquals("b1", result[0].id)
    }

    @Test
    fun `returns empty when no events match`() {
        val result = useCase(emptyList(), LocalDate.now())

        assertTrue(result.isEmpty())
    }
}
