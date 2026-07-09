package co.bleck.shammah.data.repository

import co.bleck.shammah.data.dto.SermonDto
import co.bleck.shammah.data.mapper.SermonMapper
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SermonRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : SermonRepository {

    override fun getSermons(): Flow<List<Sermon>> = callbackFlow {
        val listenerRegistration = db.collection("sermons")
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    closeOnFirestoreError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(SermonDto::class.java)?.let { dto ->
                            SermonMapper.toDomain(doc.id, dto)
                        }
                    }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
