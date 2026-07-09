package co.bleck.shammah.ui.home.sermons

import app.cash.turbine.test
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.GetSermonsUseCase
import co.bleck.shammah.fake.FakeSermonRepository
import co.bleck.shammah.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SermonsViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private lateinit var repository: FakeSermonRepository
    private lateinit var viewModel: SermonsViewModel

    @Before
    fun setUp() {
        repository = FakeSermonRepository()
        viewModel = SermonsViewModel(GetSermonsUseCase(repository))
    }

    @Test
    fun `loads sermons from use case`() = runTest {
        val sermons = listOf(Sermon(id = "s1", title = "Grace"))

        viewModel.sermons.test {
            assertEquals(emptyList<Sermon>(), awaitItem())
            repository.emit(sermons)
            assertEquals(sermons, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
