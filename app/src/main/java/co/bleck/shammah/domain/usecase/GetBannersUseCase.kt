package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBannersUseCase @Inject constructor(private val repository: BannerRepository) {
    operator fun invoke(): Flow<List<Banner>> = repository.getBanners()
}
