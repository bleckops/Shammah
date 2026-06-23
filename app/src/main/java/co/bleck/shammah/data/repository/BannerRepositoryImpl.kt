package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BannerRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : BannerRepository {

    override fun getBanners(): Flow<List<Banner>> = callbackFlow {
        val listenerRegistration = db.collection("banners")
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    closeOnFirestoreError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        val banner = doc.toObject(Banner::class.java)
                        banner?.copy(
                            id = doc.id,
                            imageUrl = (banner.imageUrl as? String).orEmpty(),
                            title = (banner.title as? String).orEmpty(),
                            linkUrl = banner.linkUrl as? String,
                            audioUrl = (banner.audioUrl as? String).orEmpty(),
                            category = (banner.category as? String).orEmpty(),
                            speaker = (banner.speaker as? String).orEmpty(),
                            videoUrl = (banner.videoUrl as? String).orEmpty(),
                            order = banner.order,
                            isActive = banner.isActive,
                            createdAt = banner.createdAt ?: java.util.Date(),
                            updatedAt = banner.updatedAt ?: java.util.Date()
                        )
                    }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
