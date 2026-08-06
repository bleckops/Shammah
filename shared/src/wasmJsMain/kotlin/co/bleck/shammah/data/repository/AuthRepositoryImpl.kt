@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package co.bleck.shammah.data.repository

import co.bleck.shammah.data.firebase.js.ShammahFirebaseJs
import co.bleck.shammah.data.firebase.js.jsAnyToKotlinStringOrNull
import co.bleck.shammah.data.firebase.js.jsSignInAnonymously
import co.bleck.shammah.data.firebase.js.jsSignOut
import co.bleck.shammah.data.firebase.parseFirebaseUserJson
import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.js.ExperimentalWasmJsInterop

class AuthRepositoryImpl : AuthRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        ShammahFirebaseJs.onAuthStateChanged { userJson ->
            val json = jsAnyToKotlinStringOrNull(userJson)
            _currentUser.value = if (json.isNullOrBlank()) {
                null
            } else {
                runCatching {
                    val (uid, isAnonymous) = parseFirebaseUserJson(json)
                    User(id = uid, isAnonymous = isAnonymous)
                }.getOrNull()
            }
        }
    }

    override suspend fun signInAnonymously(): Result<User> = runCatching {
        val json = jsSignInAnonymously()
        val (uid, isAnonymous) = parseFirebaseUserJson(json)
        val user = User(id = uid, isAnonymous = isAnonymous)
        _currentUser.value = user
        user
    }

    override fun signOut() {
        scope.launch {
            runCatching { jsSignOut() }
            _currentUser.value = null
        }
    }
}
