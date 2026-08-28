package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.testsupport.FakeSermonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetSermonsUseCaseTest {
    private val repository = FakeSermonRepository()
    private val useCase = GetSermonsUseCase(repository)

    @Test
    fun returnsEmptyListWhenRepositoryHasNoSermons() = runTest {
        assertTrue(useCase().first().isEmpty())
    }

    @Test
    fun returnsSermonsEmittedByRepository() = runTest {
        repository.emit(
            listOf(
                Sermon(
                    id = "s1",
                    title = "The Good Shepherd",
                    description = "John 10:11",
                    isActive = true,
                ),
            ),
        )

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("s1", result[0].id)
        assertEquals("The Good Shepherd", result[0].title)
    }

    @Test
    fun propagatesFlowUpdatesFromRepository() = runTest {
        repository.emit(listOf(Sermon(id = "s1", title = "First Sermon")))
        assertEquals(1, useCase().first().size)

        repository.emit(
            listOf(
                Sermon(id = "s1", title = "First Sermon"),
                Sermon(id = "s2", title = "Second Sermon"),
            ),
        )
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("s2", updated[1].id)
    }

    @Test
    fun sermonWithEmptyNotesIsPreserved() = runTest {
        repository.emit(listOf(Sermon(id = "s1", title = "No Notes Sermon", notes = "")))
        assertEquals("", useCase().first()[0].notes)
    }

    @Test
    fun sermonTitleIsPreservedExactly() = runTest {
        val title = "Grace & Peace — Romans 1:7"
        repository.emit(listOf(Sermon(id = "s1", title = title)))
        assertEquals(title, useCase().first()[0].title)
    }
}
