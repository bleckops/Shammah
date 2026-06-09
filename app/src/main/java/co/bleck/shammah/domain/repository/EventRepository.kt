package co.bleck.shammah.domain.repository

import co.bleck.shammah.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
}
