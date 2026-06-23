package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.repository.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class EventRepositoryImpl : EventRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun getEvents(): Flow<List<Event>> = callbackFlow {
        val listenerRegistration = db.collection("events")
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        val event = doc.toObject(Event::class.java)
                        event?.copy(
                            id = doc.id,
                            title = (event.title as? String).orEmpty(),
                            description = (event.description as? String).orEmpty(),
                            date = event.date ?: java.util.Date(),
                            time = (event.time as? String).orEmpty(),
                            location = (event.location as? String).orEmpty(),
                            imageUrl = (event.imageUrl as? String).orEmpty(),
                            type = event.type ?: co.bleck.shammah.domain.model.EventType.social,
                            isActive = event.isActive,
                            createdAt = event.createdAt ?: java.util.Date(),
                            updatedAt = event.updatedAt ?: java.util.Date()
                        )
                    }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
