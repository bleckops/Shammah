package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveCurrentUserUseCase(private val repository: AuthRepository) {
    operator fun invoke(): StateFlow<User?> = repository.currentUser
}
