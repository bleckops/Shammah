package co.bleck.shammah.composeapp.ui.home.sermons

import app.cash.turbine.test
import co.bleck.shammah.testsupport.FakeSermonRepository
import co.bleck.shammah.testsupport.MainDispatcherTest
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.GetSermonsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SermonsViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeSermonRepository
    private lateinit var viewModel: SermonsViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()
        repository = FakeSermonRepository()
        viewModel = SermonsViewModel(GetSermonsUseCase(repository))
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun loadsSermonsFromUseCase() = runTest {
        val sermons = listOf(Sermon(id = "s1", title = "Grace"))

        viewModel.sermons.test {
            assertEquals(emptyList(), awaitItem())
            repository.emit(sermons)
            assertEquals(sermons, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
