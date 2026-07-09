package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class ProjectEventDatesUseCase @Inject constructor() {

    operator fun invoke(
        events: List<Event>,
        today: LocalDate = LocalDate.now(),
        yearRange: IntRange = -1..2
    ): Map<LocalDate, List<Event>> {
        val result = mutableMapOf<LocalDate, MutableList<Event>>()
        events.forEach { event ->
            val rawDate = event.date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            if (event.type == EventType.birthdays) {
                for (yearOffset in yearRange) {
                    val key = rawDate.withYear(today.year + yearOffset)
                    result.getOrPut(key) { mutableListOf() }.add(event)
                }
            } else {
                result.getOrPut(rawDate) { mutableListOf() }.add(event)
            }
        }
        return result
    }
}
