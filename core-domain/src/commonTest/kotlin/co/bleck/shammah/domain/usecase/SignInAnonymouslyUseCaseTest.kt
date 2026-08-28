package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.testsupport.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignInAnonymouslyUseCaseTest {
    private val repository = FakeAuthRepository()
    private val useCase = SignInAnonymouslyUseCase(repository)

    @Test
    fun returnsUserOnSuccess() = runTest {
        val user = User(id = "u1", isAnonymous = true)
        repository.signInResult = Result.success(user)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun propagatesFailure() = runTest {
        repository.signInResult = Result.failure(Exception("failed"))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("failed", result.exceptionOrNull()?.message)
    }
}
