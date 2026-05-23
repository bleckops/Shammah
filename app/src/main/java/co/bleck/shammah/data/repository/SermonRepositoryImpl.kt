package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.repository.SermonRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SermonRepositoryImpl : SermonRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun getSermons(): Flow<List<Sermon>> = callbackFlow {
        val listenerRegistration = db.collection("sermons")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(Sermon::class.java) }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
