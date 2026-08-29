package co.bleck.shammah.domain.model

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EventRecurrenceTest {
    @Test
    fun finiteDailyCountsTheOriginalOccurrence() {
        val event = Event(repeat = RECURSIVE_REPEAT, repeatNumber = 3, period = DAILY_PERIOD)
        val start = LocalDate(2025, 1, 1)
        assertTrue(event.matchesDate(LocalDate(2025, 1, 1), start))
        assertTrue(event.matchesDate(LocalDate(2025, 1, 3), start))
        assertFalse(event.matchesDate(LocalDate(2025, 1, 4), start))
    }

    @Test
    fun monthlyRecurrenceClampsMonthEnd() {
        val event = Event(repeat = RECURSIVE_REPEAT, repeatNumber = 3, period = MONTHLY_PERIOD)
        val start = LocalDate(2025, 1, 31)
        assertTrue(event.matchesDate(LocalDate(2025, 2, 28), start))
        assertTrue(event.matchesDate(LocalDate(2025, 3, 31), start))
        assertFalse(event.matchesDate(LocalDate(2025, 2, 27), start))
    }

    @Test
    fun unlimitedBirthdayMatchesMonthAndDay() {
        val event = Event(type = EventType.birthdays, repeat = RECURSIVE_REPEAT, period = YEARLY_PERIOD)
        assertTrue(event.matchesDate(LocalDate(2030, 6, 15), LocalDate(1990, 6, 15)))
        assertFalse(event.matchesDate(LocalDate(2030, 6, 16), LocalDate(1990, 6, 15)))
    }
}
