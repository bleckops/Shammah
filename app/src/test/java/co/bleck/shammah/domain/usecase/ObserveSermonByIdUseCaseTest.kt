package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.fake.FakeSermonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveSermonByIdUseCaseTest {

    private lateinit var repository: FakeSermonRepository
    private lateinit var useCase: ObserveSermonByIdUseCase

    @Before
    fun setUp() {
        repository = FakeSermonRepository()
        useCase = ObserveSermonByIdUseCase(repository)
    }

    @Test
    fun `returns matching sermon by id`() = runTest {
        repository.emit(
            listOf(
                Sermon(id = "s1", title = "First"),
                Sermon(id = "s2", title = "Second")
            )
        )

        val result = useCase("s2").first()

        assertEquals("s2", result?.id)
        assertEquals("Second", result?.title)
    }

    @Test
    fun `returns null when sermon not found`() = runTest {
        repository.emit(listOf(Sermon(id = "s1", title = "First")))

        val result = useCase("missing").first()

        assertNull(result)
    }
}
