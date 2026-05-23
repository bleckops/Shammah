package co.bleck.shammah.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.data.repository.AuthRepositoryImpl
import co.bleck.shammah.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: FirebaseUser) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    val currentUser: StateFlow<FirebaseUser?> = repository.currentUser

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signInAnonymously() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.signInAnonymously()
                .onSuccess { user ->
                    _uiState.value = AuthUiState.Success(user)
                }
                .onFailure { exception ->
                    _uiState.value = AuthUiState.Error(exception.localizedMessage ?: "Error desconocido")
                }
        }
    }

    fun signOut() {
        repository.signOut()
        _uiState.value = AuthUiState.Idle
    }
}
