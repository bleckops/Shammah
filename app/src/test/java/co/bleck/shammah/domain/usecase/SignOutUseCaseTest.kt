package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.fake.FakeAuthRepository
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class SignOutUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var useCase: SignOutUseCase

    @Before
    fun setUp() {
        repository = FakeAuthRepository(initialUser = User(id = "u1", isAnonymous = true))
        useCase = SignOutUseCase(repository)
    }

    @Test
    fun `clears current user`() {
        useCase()

        assertNull(repository.currentUser.value)
    }
}
