package co.bleck.shammah.composeapp.ui.auth

import co.bleck.shammah.composeapp.fake.FakeAuthRepository
import co.bleck.shammah.composeapp.util.MainDispatcherTest
import co.bleck.shammah.domain.model.User
import co.bleck.shammah.domain.usecase.ObserveCurrentUserUseCase
import co.bleck.shammah.domain.usecase.SignInAnonymouslyUseCase
import co.bleck.shammah.domain.usecase.SignOutUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    override fun setUp() {
        super.setUp()
        repository = FakeAuthRepository()
        viewModel = AuthViewModel(
            observeCurrentUser = ObserveCurrentUserUseCase(repository),
            signInAnonymouslyUseCase = SignInAnonymouslyUseCase(repository),
            signOutUseCase = SignOutUseCase(repository),
        )
    }

    @Test
    fun signInAnonymouslySetsSuccessState() = runTest {
        val user = User(id = "u1", isAnonymous = true)
        repository.signInResult = Result.success(user)

        viewModel.signInAnonymously()

        assertEquals(AuthUiState.Success(user), viewModel.uiState.value)
    }

    @Test
    fun signInAnonymouslySetsErrorStateOnFailure() = runTest {
        repository.signInResult = Result.failure(Exception("Auth failed"))

        viewModel.signInAnonymously()

        val error = viewModel.uiState.value
        assertTrue(error is AuthUiState.Error)
        assertEquals("Auth failed", (error as AuthUiState.Error).message)
    }

    @Test
    fun signOutResetsUiStateToIdle() = runTest {
        repository.setUser(User(id = "u1", isAnonymous = true))

        viewModel.signOut()

        assertEquals(AuthUiState.Idle, viewModel.uiState.value)
        assertNull(viewModel.currentUser.value)
    }
}
