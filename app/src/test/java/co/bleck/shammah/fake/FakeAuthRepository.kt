package co.bleck.shammah.fake

import co.bleck.shammah.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthRepository(initialUser: FirebaseUser? = null) : AuthRepository {

    private val _currentUser = MutableStateFlow(initialUser)
    override val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    /** Controls whether the next [signInAnonymously] call succeeds or fails. */
    var signInResult: Result<FirebaseUser> = Result.failure(IllegalStateException("No mock user configured"))

    /** Simulates a user becoming signed in or out externally. */
    fun setUser(user: FirebaseUser?) {
        _currentUser.value = user
    }

    override suspend fun signInAnonymously(): Result<FirebaseUser> = signInResult

    override fun signOut() {
        _currentUser.value = null
    }
}
