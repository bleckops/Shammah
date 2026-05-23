package co.bleck.shammah.domain.repository

import co.bleck.shammah.domain.model.Banner
import kotlinx.coroutines.flow.Flow

interface BannerRepository {
    fun getBanners(): Flow<List<Banner>>
}
