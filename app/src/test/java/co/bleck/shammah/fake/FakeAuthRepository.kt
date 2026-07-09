package co.bleck.shammah.fake

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.repository.AuthRepository
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
