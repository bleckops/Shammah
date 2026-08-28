@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.data.pipeline

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.data.mapper.EventMapper
import co.bleck.shammah.domain.model.EventType
import co.bleck.shammah.domain.usecase.FilterEventsForDateUseCase
import co.bleck.shammah.domain.usecase.ProjectEventDatesUseCase
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Lightweight integration-style coverage for the DTO → domain → calendar use-case chain
 * without a live Firebase connection.
 */
class FirestoreToDomainPipelineTest {
    private val timeZone = TimeZone.UTC
    private val filter = FilterEventsForDateUseCase(timeZone)
    private val project = ProjectEventDatesUseCase(timeZone)

    private fun instant(year: Int, month: Int, day: Int): Instant =
        LocalDate(year, month, day).atStartOfDayIn(timeZone)

    @Test
    fun mapsFirestoreLikePayloadsAndFiltersCalendarDay() {
        val docs = listOf(
            "e1" to EventDto(
                title = "Camp",
                type = "camp",
                date = instant(2025, 7, 1),
                location = "Mountain",
            ),
            "e2" to EventDto(
                title = "Birthday",
                type = "birthdays",
                date = instant(1990, 7, 1),
            ),
            "e3" to EventDto(
                title = "Other day",
                type = "social",
                date = instant(2025, 7, 2),
            ),
        )

        val events = docs.map { (id, dto) -> EventMapper.toDomain(id, dto) }
        val forDay = filter(events, LocalDate(2025, 7, 1))

        assertEquals(2, forDay.size)
        assertEquals(setOf("e1", "e2"), forDay.map { it.id }.toSet())
        assertEquals(EventType.camp, forDay.first { it.id == "e1" }.type)
        assertEquals(EventType.birthdays, forDay.first { it.id == "e2" }.type)
    }

    @Test
    fun projectsMappedBirthdaysAcrossRequestedYears() {
        val events = listOf(
            EventMapper.toDomain(
                "b1",
                EventDto(
                    title = "Birthday",
                    type = "birthdays",
                    date = instant(1990, 6, 15),
                ),
            ),
        )

        val projected = project(events, today = LocalDate(2025, 6, 15), yearRange = 0..1)
        assertEquals(2, projected.size)
        assertTrue(projected.containsKey(LocalDate(2025, 6, 15)))
        assertTrue(projected.containsKey(LocalDate(2026, 6, 15)))
    }
}
