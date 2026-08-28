package co.bleck.shammah.composeapp.ui.home.sermons.detail

import app.cash.turbine.test
import co.bleck.shammah.testsupport.FakeSermonRepository
import co.bleck.shammah.testsupport.MainDispatcherTest
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.ObserveSermonByIdUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SermonDetailViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeSermonRepository
    private lateinit var viewModel: SermonDetailViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()
        repository = FakeSermonRepository()
        viewModel = SermonDetailViewModel(
            sermonId = "s2",
            observeSermonByIdUseCase = ObserveSermonByIdUseCase(repository),
        )
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
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
