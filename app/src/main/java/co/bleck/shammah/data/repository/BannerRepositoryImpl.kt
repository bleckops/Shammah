package co.bleck.shammah.data.repository

import co.bleck.shammah.data.dto.BannerDto
import co.bleck.shammah.data.mapper.BannerMapper
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BannerRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : BannerRepository {

    override fun getBanners(): Flow<List<Banner>> = callbackFlow {
        val listenerRegistration = db.collection("banners")
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    closeOnFirestoreError(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {3
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(BannerDto::class.java)?.let { dto ->
                            BannerMapper.toDomain(doc.id, dto)
                        }
                    }
                    trySend(list)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
