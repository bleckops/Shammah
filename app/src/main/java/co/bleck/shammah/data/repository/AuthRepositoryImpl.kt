package co.bleck.shammah.data.repository

import co.bleck.shammah.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthRepositoryImpl private constructor() : AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    override val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthRepositoryImpl? = null

        fun getInstance(): AuthRepositoryImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthRepositoryImpl().also { INSTANCE = it }
            }
    }

    override suspend fun signInAnonymously(): Result<FirebaseUser> = suspendCancellableCoroutine { continuation ->
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        continuation.resume(Result.success(user))
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
}
