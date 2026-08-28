package co.bleck.shammah.domain.repository

import co.bleck.shammah.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    suspend fun signInAnonymously(): Result<User>
    fun signOut()
}
