package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.fake.FakeAuthRepository
import kotlin.test.Test
import kotlin.test.assertNull

class SignOutUseCaseTest {
    private val repository = FakeAuthRepository(initialUser = User(id = "u1", isAnonymous = true))
    private val useCase = SignOutUseCase(repository)

    @Test
    fun clearsCurrentUser() {
        useCase()
        assertNull(repository.currentUser.value)
    }
}
