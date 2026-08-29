@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.matchesDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FilterEventsForDateUseCase(
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    operator fun invoke(events: List<Event>, date: LocalDate): List<Event> =
        events.filter { event ->
            event.matchesDate(date, event.date.toLocalDateTime(timeZone).date)
        }
}
