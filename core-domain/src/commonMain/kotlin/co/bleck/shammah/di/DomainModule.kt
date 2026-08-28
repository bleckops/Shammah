package co.bleck.shammah.di

import co.bleck.shammah.domain.usecase.FilterEventsForDateUseCase
import co.bleck.shammah.domain.usecase.GetBannersUseCase
import co.bleck.shammah.domain.usecase.GetEventsUseCase
import co.bleck.shammah.domain.usecase.GetMissionVisionAboutUsResourcesUseCase
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

val domainModule = module {
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
    factory { GetMissionVisionAboutUsResourcesUseCase(get()) }
}
