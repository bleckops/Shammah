@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FilterEventsForDateUseCaseTest {
    private val useCase = FilterEventsForDateUseCase(TimeZone.UTC)

    private fun dateAt(year: Int, month: Int, day: Int): Instant =
        LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC)

    @Test
    fun returnsEventsOnExactDate() {
        val target = LocalDate(2025, 6, 15)
        val events = listOf(
            Event(id = "e1", title = "Match", date = dateAt(2025, 6, 15)),
            Event(id = "e2", title = "Other", date = dateAt(2025, 6, 16)),
        )

        val result = useCase(events, target)
        assertEquals(1, result.size)
        assertEquals("e1", result.first().id)
    }

    @Test
    fun birthdayMatchesByMonthAndDay() {
        val target = LocalDate(2025, 3, 10)
        val events = listOf(
            Event(id = "b1", type = EventType.birthdays, date = dateAt(1990, 3, 10)),
            Event(id = "b2", type = EventType.birthdays, date = dateAt(1990, 3, 11)),
        )

        val result = useCase(events, target)
        assertEquals(1, result.size)
        assertEquals("b1", result.first().id)
    }

    @Test
    fun returnsEmptyWhenNoEventsMatch() {
        assertTrue(useCase(emptyList(), LocalDate(2025, 1, 1)).isEmpty())
    }
}
