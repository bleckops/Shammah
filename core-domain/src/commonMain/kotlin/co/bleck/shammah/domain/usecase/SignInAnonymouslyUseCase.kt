package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.repository.AuthRepository

class SignInAnonymouslyUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<User> = repository.signInAnonymously()
}
