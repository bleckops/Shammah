package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.testsupport.FakeSermonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ObserveSermonByIdUseCaseTest {
    private val repository = FakeSermonRepository()
    private val useCase = ObserveSermonByIdUseCase(repository)

    @Test
    fun returnsMatchingSermonById() = runTest {
        repository.emit(listOf(Sermon(id = "s1", title = "First"), Sermon(id = "s2", title = "Second")))
        val result = useCase("s2").first()
        assertEquals("s2", result?.id)
    }

    @Test
    fun returnsNullWhenSermonNotFound() = runTest {
        repository.emit(listOf(Sermon(id = "s1", title = "First")))
        val result = useCase("missing").first()
        assertNull(result)
    }
}
