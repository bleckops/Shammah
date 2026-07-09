package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.fake.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SignInAnonymouslyUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var useCase: SignInAnonymouslyUseCase

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        useCase = SignInAnonymouslyUseCase(repository)
    }

    @Test
    fun `returns user on success`() = runTest {
        val user = User(id = "u1", isAnonymous = true)
        repository.signInResult = Result.success(user)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `propagates failure`() = runTest {
        repository.signInResult = Result.failure(Exception("failed"))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("failed", result.exceptionOrNull()?.message)
    }
}
