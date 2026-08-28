package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow

class GetBannersUseCase(private val repository: BannerRepository) {
    operator fun invoke(): Flow<List<Banner>> = repository.getBanners()
}
