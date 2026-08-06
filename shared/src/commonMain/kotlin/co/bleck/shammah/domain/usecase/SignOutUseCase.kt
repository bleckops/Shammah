package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.signOut()
}
