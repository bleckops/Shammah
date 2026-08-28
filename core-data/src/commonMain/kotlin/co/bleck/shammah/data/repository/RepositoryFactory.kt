package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.repository.AuthRepository
import co.bleck.shammah.domain.repository.BannerRepository
import co.bleck.shammah.domain.repository.EventRepository
import co.bleck.shammah.domain.repository.ResourceRepository
import co.bleck.shammah.domain.repository.SermonRepository

internal expect fun createAuthRepository(): AuthRepository
internal expect fun createBannerRepository(): BannerRepository
internal expect fun createEventRepository(): EventRepository
internal expect fun createResourceRepository(): ResourceRepository
internal expect fun createSermonRepository(): SermonRepository
