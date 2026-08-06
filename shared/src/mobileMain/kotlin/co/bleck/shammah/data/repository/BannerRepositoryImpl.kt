package co.bleck.shammah.data.repository

import co.bleck.shammah.data.mapper.BannerMapper
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow

class BannerRepositoryImpl : BannerRepository {
    override fun getBanners(): Flow<List<Banner>> = firestoreActiveCollectionFlow("banners") { id, document ->
        BannerMapper.toDomain(id, document.toBannerDto())
    }
}
