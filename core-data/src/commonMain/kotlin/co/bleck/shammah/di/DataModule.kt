package co.bleck.shammah.di

import co.bleck.shammah.data.repository.createAuthRepository
import co.bleck.shammah.data.repository.createBannerRepository
import co.bleck.shammah.data.repository.createEventRepository
import co.bleck.shammah.data.repository.createResourceRepository
import co.bleck.shammah.data.repository.createSermonRepository
import co.bleck.shammah.domain.repository.AuthRepository
import co.bleck.shammah.domain.repository.BannerRepository
import co.bleck.shammah.domain.repository.EventRepository
import co.bleck.shammah.domain.repository.ResourceRepository
import co.bleck.shammah.domain.repository.SermonRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> { createAuthRepository() }
    single<BannerRepository> { createBannerRepository() }
    single<EventRepository> { createEventRepository() }
    single<ResourceRepository> { createResourceRepository() }
    single<SermonRepository> { createSermonRepository() }
}
