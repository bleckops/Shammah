@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FilterEventsForDateUseCase(
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    operator fun invoke(events: List<Event>, date: LocalDate): List<Event> =
        events.filter { event ->
            val eventDate = event.date.toLocalDateTime(timeZone).date
            if (event.type == EventType.birthdays) {
                eventDate.month == date.month && eventDate.day == date.day
            } else {
                eventDate == date
            }
        }
}
