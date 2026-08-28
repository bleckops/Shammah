@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
            if (event.type == EventType.birthdays) {
                for (yearOffset in yearRange) {
                    val key = LocalDate(today.year + yearOffset, rawDate.month, rawDate.day)
                    result.getOrPut(key) { mutableListOf() }.add(event)
                }
            } else {
                result.getOrPut(rawDate) { mutableListOf() }.add(event)
            }
        }
        return result
    }
}
