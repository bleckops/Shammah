package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.repository.AuthRepository
import co.bleck.shammah.domain.repository.BannerRepository
import co.bleck.shammah.domain.repository.EventRepository
import co.bleck.shammah.domain.repository.SermonRepository

internal actual fun createAuthRepository(): AuthRepository = AuthRepositoryImpl()
internal actual fun createBannerRepository(): BannerRepository = BannerRepositoryImpl()
internal actual fun createEventRepository(): EventRepository = EventRepositoryImpl()
internal actual fun createSermonRepository(): SermonRepository = SermonRepositoryImpl()
