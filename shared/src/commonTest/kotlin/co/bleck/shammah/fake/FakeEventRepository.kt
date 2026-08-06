package co.bleck.shammah.fake

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeEventRepository(initialEvents: List<Event> = emptyList()) : EventRepository {
    private val events = MutableStateFlow(initialEvents)

    fun emit(value: List<Event>) {
        events.value = value
    }

    override fun getEvents(): Flow<List<Event>> = events.asStateFlow()
}
