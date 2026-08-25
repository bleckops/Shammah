package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveEventByIdUseCase(private val repository: EventRepository) {
    operator fun invoke(eventId: String): Flow<Event?> =
        repository.getEvents().map { events -> events.find { it.id == eventId } }
}
