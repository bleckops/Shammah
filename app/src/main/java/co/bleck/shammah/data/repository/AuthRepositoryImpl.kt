package co.bleck.shammah.data.repository

import co.bleck.shammah.data.mapper.UserMapper
import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val _currentUser = MutableStateFlow(auth.currentUser?.toDomain())
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser?.toDomain()
        }
    }

    override suspend fun signInAnonymously(): Result<User> = suspendCancellableCoroutine { continuation ->
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        continuation.resume(Result.success(user.toDomain()))
                    } else {
                        continuation.resume(Result.failure(Exception("Usuario nulo tras autenticación")))
                    }
                } else {
                    continuation.resume(Result.failure(task.exception ?: Exception("Error en autenticación anónima")))
                }
            }
    }

    override fun signOut() {
        auth.signOut()
    }

    private fun FirebaseUser.toDomain(): User = UserMapper.toDomain(this)
}
