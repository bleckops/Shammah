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
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(Event::class.java) }
                        .filter { it.isActive }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
