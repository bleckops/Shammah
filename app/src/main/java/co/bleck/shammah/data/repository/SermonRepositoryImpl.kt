package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SermonRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
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
                        val sermon = doc.toObject(Sermon::class.java)
                        sermon?.copy(
                            id = doc.id,
                            title = (sermon.title as? String).orEmpty(),
                            description = (sermon.description as? String).orEmpty(),
                            date = sermon.date ?: java.util.Date(),
                            notes = (sermon.notes as? String).orEmpty(),
                            isActive = sermon.isActive,
                            createdAt = sermon.createdAt ?: java.util.Date(),
                            updatedAt = sermon.updatedAt ?: java.util.Date()
                        )
                    }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
