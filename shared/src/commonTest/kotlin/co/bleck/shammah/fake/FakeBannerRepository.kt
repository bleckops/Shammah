package co.bleck.shammah.fake

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeBannerRepository(initialBanners: List<Banner> = emptyList()) : BannerRepository {
    private val _banners = MutableStateFlow(initialBanners)

    fun emit(banners: List<Banner>) {
        _banners.value = banners
    }

    override fun getBanners(): Flow<List<Banner>> = _banners.asStateFlow()
}
