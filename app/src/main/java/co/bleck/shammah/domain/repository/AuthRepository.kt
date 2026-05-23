package co.bleck.shammah.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<FirebaseUser?>
    suspend fun signInAnonymously(): Result<FirebaseUser>
    fun signOut()
}
