package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class FilterEventsForDateUseCase @Inject constructor() {

    operator fun invoke(events: List<Event>, date: LocalDate): List<Event> =
        events.filter { event ->
            val eventDate = event.date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            if (event.type == EventType.birthdays) {
                eventDate.monthValue == date.monthValue &&
                    eventDate.dayOfMonth == date.dayOfMonth
            } else {
                eventDate == date
            }
        }
}
