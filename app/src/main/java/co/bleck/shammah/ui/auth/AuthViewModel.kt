package co.bleck.shammah.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.usecase.ObserveCurrentUserUseCase
import co.bleck.shammah.domain.usecase.SignInAnonymouslyUseCase
import co.bleck.shammah.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    observeCurrentUser: ObserveCurrentUserUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val currentUser: StateFlow<User?> = observeCurrentUser()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signInAnonymously() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            signInAnonymouslyUseCase()
                .onSuccess { user ->
                    _uiState.value = AuthUiState.Success(user)
                }
                .onFailure { exception ->
                    _uiState.value = AuthUiState.Error(exception.localizedMessage ?: "Error desconocido")
                }
        }
    }

    fun signOut() {
        signOutUseCase()
        _uiState.value = AuthUiState.Idle
    }
}
