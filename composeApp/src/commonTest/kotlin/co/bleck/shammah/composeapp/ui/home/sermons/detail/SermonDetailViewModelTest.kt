package co.bleck.shammah.composeapp.ui.home.sermons.detail

import app.cash.turbine.test
import co.bleck.shammah.composeapp.fake.FakeSermonRepository
import co.bleck.shammah.composeapp.util.MainDispatcherTest
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.ObserveSermonByIdUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SermonDetailViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeSermonRepository
    private lateinit var viewModel: SermonDetailViewModel

    override fun setUp() {
        super.setUp()
        repository = FakeSermonRepository()
        viewModel = SermonDetailViewModel(
            sermonId = "s2",
            observeSermonByIdUseCase = ObserveSermonByIdUseCase(repository),
        )
    }

    @Test
    fun loadsSermonMatchingNavigationId() = runTest {
        viewModel.sermon.test {
            assertNull(awaitItem())
            repository.emit(
                listOf(
                    Sermon(id = "s1", title = "First"),
                    Sermon(id = "s2", title = "Second"),
                ),
            )
            val sermon = awaitItem()
            assertEquals("s2", sermon?.id)
            assertEquals("Second", sermon?.title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
