package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.repository.AuthRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthRepositoryImpl : AuthRepository {
    private val auth = Firebase.auth
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _currentUser = MutableStateFlow(auth.currentUser?.toDomain())
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        scope.launch {
            auth.authStateChanged.collect { firebaseUser ->
                _currentUser.value = firebaseUser?.toDomain()
            }
        }
    }

    override suspend fun signInAnonymously(): Result<User> = runCatching {
        val user = auth.signInAnonymously().user
            ?: error("Usuario nulo tras autenticación")
        user.toDomain()
    }

    override fun signOut() {
        scope.launch {
            runCatching { auth.signOut() }
            _currentUser.value = null
        }
    }

    private fun FirebaseUser.toDomain(): User = User(
        id = uid,
        isAnonymous = isAnonymous,
    )
}
