package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.User
import co.bleck.shammah.testsupport.FakeAuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ObserveCurrentUserUseCaseTest {
    @Test
    fun exposesRepositoryCurrentUser() {
        val user = User(id = "u1", isAnonymous = true)
        val repository = FakeAuthRepository(initialUser = user)
        val useCase = ObserveCurrentUserUseCase(repository)

        assertEquals(user, useCase().value)

        repository.setUser(null)
        assertNull(useCase().value)
    }
}
