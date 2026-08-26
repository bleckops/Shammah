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
import co.bleck.shammah.domain.usecase.FilterEventsForDateUseCase
import co.bleck.shammah.domain.usecase.GetBannersUseCase
import co.bleck.shammah.domain.usecase.GetEventsUseCase
import co.bleck.shammah.domain.usecase.GetPublicResourcesUseCase
import co.bleck.shammah.domain.usecase.GetSermonsUseCase
import co.bleck.shammah.domain.usecase.ObserveAboutContentUseCase
import co.bleck.shammah.domain.usecase.ObserveCurrentUserUseCase
import co.bleck.shammah.domain.usecase.ObserveEventByIdUseCase
import co.bleck.shammah.domain.usecase.ObserveSermonByIdUseCase
import co.bleck.shammah.domain.usecase.ProjectEventDatesUseCase
import co.bleck.shammah.domain.usecase.SignInAnonymouslyUseCase
import co.bleck.shammah.domain.usecase.SignOutUseCase
import org.koin.dsl.module

val sharedModule = module {
    single<AuthRepository> { createAuthRepository() }
    single<BannerRepository> { createBannerRepository() }
    single<EventRepository> { createEventRepository() }
    single<ResourceRepository> { createResourceRepository() }
    single<SermonRepository> { createSermonRepository() }

    factory { GetBannersUseCase(get()) }
    factory { GetEventsUseCase(get()) }
    factory { GetSermonsUseCase(get()) }
    factory { GetPublicResourcesUseCase(get()) }
    factory { ObserveAboutContentUseCase(get()) }
    factory { ObserveCurrentUserUseCase(get()) }
    factory { ObserveSermonByIdUseCase(get()) }
    factory { ObserveEventByIdUseCase(get()) }
    factory { SignInAnonymouslyUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { FilterEventsForDateUseCase() }
    factory { ProjectEventDatesUseCase() }
}
