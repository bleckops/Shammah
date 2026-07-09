package co.bleck.shammah.ui.auth

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.usecase.ObserveCurrentUserUseCase
import co.bleck.shammah.domain.usecase.SignInAnonymouslyUseCase
import co.bleck.shammah.domain.usecase.SignOutUseCase
import co.bleck.shammah.fake.FakeAuthRepository
import co.bleck.shammah.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        viewModel = AuthViewModel(
            observeCurrentUser = ObserveCurrentUserUseCase(repository),
            signInAnonymouslyUseCase = SignInAnonymouslyUseCase(repository),
            signOutUseCase = SignOutUseCase(repository)
        )
    }

    @Test
    fun `signInAnonymously sets success state`() = runTest {
        val user = User(id = "u1", isAnonymous = true)
        repository.signInResult = Result.success(user)

        viewModel.signInAnonymously()

        assertEquals(AuthUiState.Success(user), viewModel.uiState.value)
    }

    @Test
    fun `signInAnonymously sets error state on failure`() = runTest {
        repository.signInResult = Result.failure(Exception("Auth failed"))

        viewModel.signInAnonymously()

        val error = viewModel.uiState.value as AuthUiState.Error
        assertEquals("Auth failed", error.message)
    }

    @Test
    fun `signOut resets ui state to idle`() = runTest {
        repository.setUser(User(id = "u1", isAnonymous = true))

        viewModel.signOut()

        assertEquals(AuthUiState.Idle, viewModel.uiState.value)
        assertNull(viewModel.currentUser.value)
    }
}
