package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.fake.FakeSermonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class GetSermonsUseCaseTest {

    private lateinit var repository: FakeSermonRepository
    private lateinit var useCase: GetSermonsUseCase

    @Before
    fun setUp() {
        repository = FakeSermonRepository()
        useCase = GetSermonsUseCase(repository)
    }

    @Test
    fun `returns empty list when repository has no sermons`() = runTest {
        val result = useCase().first()
        assertTrue("Expected empty list", result.isEmpty())
    }

    @Test
    fun `returns sermons emitted by repository`() = runTest {
        val sermons = listOf(
            Sermon(
                id = "s1",
                title = "The Good Shepherd",
                description = "John 10:11",
                date = Date(),
                isActive = true
            )
        )
        repository.emit(sermons)

        val result = useCase().first()
        assertEquals(1, result.size)
        assertEquals("s1", result[0].id)
        assertEquals("The Good Shepherd", result[0].title)
    }

    @Test
    fun `propagates flow updates from repository`() = runTest {
        val first = listOf(Sermon(id = "s1", title = "First Sermon"))
        val second = listOf(
            Sermon(id = "s1", title = "First Sermon"),
            Sermon(id = "s2", title = "Second Sermon")
        )

        repository.emit(first)
        assertEquals(1, useCase().first().size)

        repository.emit(second)
        val updated = useCase().first()
        assertEquals(2, updated.size)
        assertEquals("s2", updated[1].id)
    }

    @Test
    fun `sermon with empty notes is preserved`() = runTest {
        val sermon = Sermon(id = "s1", title = "No Notes Sermon", notes = "")
        repository.emit(listOf(sermon))

        val result = useCase().first()
        assertEquals("", result[0].notes)
    }

    @Test
    fun `sermon title is preserved exactly`() = runTest {
        val title = "Grace & Peace — Romans 1:7"
        val sermon = Sermon(id = "s1", title = title)
        repository.emit(listOf(sermon))

        val result = useCase().first()
        assertEquals(title, result[0].title)
    }
}
