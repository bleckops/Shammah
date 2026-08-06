package co.bleck.shammah.data.repository

import co.bleck.shammah.data.firebase.firestoreActiveCollectionFlow
import co.bleck.shammah.data.firebase.toBannerDto
import co.bleck.shammah.data.mapper.BannerMapper
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow

class BannerRepositoryImpl : BannerRepository {
    override fun getBanners(): Flow<List<Banner>> = firestoreActiveCollectionFlow("banners") { id, data ->
        BannerMapper.toDomain(id, data.toBannerDto())
    }
}
