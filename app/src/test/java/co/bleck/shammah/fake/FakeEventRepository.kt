package co.bleck.shammah.fake

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeEventRepository(initialEvents: List<Event> = emptyList()) : EventRepository {

    private val _events = MutableStateFlow(initialEvents)

    /** Emit a new list to simulate a Firestore snapshot update. */
    fun emit(events: List<Event>) {
        _events.value = events
    }

    override fun getEvents(): Flow<List<Event>> = _events.asStateFlow()
}
