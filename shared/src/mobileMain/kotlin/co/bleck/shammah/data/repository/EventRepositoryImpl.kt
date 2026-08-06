package co.bleck.shammah.data.repository

import co.bleck.shammah.data.mapper.EventMapper
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl : EventRepository {
    override fun getEvents(): Flow<List<Event>> = firestoreActiveCollectionFlow("events") { id, document ->
        EventMapper.toDomain(id, document.toEventDto())
    }
}
