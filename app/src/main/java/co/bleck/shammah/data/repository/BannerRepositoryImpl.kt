package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BannerRepositoryImpl : BannerRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun getBanners(): Flow<List<Banner>> = callbackFlow {
        val listenerRegistration = db.collection("banners")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(Banner::class.java) }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
