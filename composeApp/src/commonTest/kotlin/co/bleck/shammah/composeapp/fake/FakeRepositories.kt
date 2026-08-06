package co.bleck.shammah.composeapp.fake

import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.repository.AuthRepository
import co.bleck.shammah.domain.repository.BannerRepository
import co.bleck.shammah.domain.repository.EventRepository
import co.bleck.shammah.domain.repository.SermonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthRepository(initialUser: User? = null) : AuthRepository {
    private val _currentUser = MutableStateFlow(initialUser)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    var signInResult: Result<User> = Result.failure(IllegalStateException("No mock user configured"))

    fun setUser(user: User?) {
        _currentUser.value = user
    }

    override suspend fun signInAnonymously(): Result<User> = signInResult

    override fun signOut() {
        _currentUser.value = null
    }
}

class FakeBannerRepository(initialBanners: List<Banner> = emptyList()) : BannerRepository {
    private val _banners = MutableStateFlow(initialBanners)

    fun emit(banners: List<Banner>) {
        _banners.value = banners
    }

    override fun getBanners(): Flow<List<Banner>> = _banners.asStateFlow()
}

class FakeEventRepository(initialEvents: List<Event> = emptyList()) : EventRepository {
    private val events = MutableStateFlow(initialEvents)

    fun emit(value: List<Event>) {
        events.value = value
    }

    override fun getEvents(): Flow<List<Event>> = events.asStateFlow()
}

class FakeSermonRepository(initialSermons: List<Sermon> = emptyList()) : SermonRepository {
    private val sermons = MutableStateFlow(initialSermons)

    fun emit(value: List<Sermon>) {
        sermons.value = value
    }

    override fun getSermons(): Flow<List<Sermon>> = sermons.asStateFlow()
}
