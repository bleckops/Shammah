package co.bleck.shammah.data.repository

import co.bleck.shammah.data.dto.EventDto
import co.bleck.shammah.data.mapper.EventMapper
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.repository.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> = callbackFlow {
        val listenerRegistration = db.collection("events")
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    closeOnFirestoreError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(EventDto::class.java)?.let { dto ->
                            EventMapper.toDomain(doc.id, dto)
                        }
                    }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
