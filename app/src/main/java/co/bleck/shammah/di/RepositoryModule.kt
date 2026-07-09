package co.bleck.shammah.di

import co.bleck.shammah.data.repository.AuthRepositoryImpl
import co.bleck.shammah.data.repository.BannerRepositoryImpl
import co.bleck.shammah.data.repository.EventRepositoryImpl
import co.bleck.shammah.data.repository.SermonRepositoryImpl
import co.bleck.shammah.domain.repository.AuthRepository
import co.bleck.shammah.domain.repository.BannerRepository
import co.bleck.shammah.domain.repository.EventRepository
import co.bleck.shammah.domain.repository.SermonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBannerRepository(impl: BannerRepositoryImpl): BannerRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Binds
    @Singleton
    abstract fun bindSermonRepository(impl: SermonRepositoryImpl): SermonRepository
}
