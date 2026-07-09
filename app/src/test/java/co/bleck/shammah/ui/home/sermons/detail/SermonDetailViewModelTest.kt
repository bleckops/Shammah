package co.bleck.shammah.ui.home.sermons.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.ObserveSermonByIdUseCase
import co.bleck.shammah.fake.FakeSermonRepository
import co.bleck.shammah.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SermonDetailViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private lateinit var repository: FakeSermonRepository
    private lateinit var viewModel: SermonDetailViewModel

    @Before
    fun setUp() {
        repository = FakeSermonRepository()
        val savedStateHandle = SavedStateHandle(mapOf("sermonId" to "s2"))
        viewModel = SermonDetailViewModel(
            savedStateHandle = savedStateHandle,
            observeSermonByIdUseCase = ObserveSermonByIdUseCase(repository)
        )
    }

    @Test
    fun `loads sermon matching navigation id`() = runTest {
        viewModel.sermon.test {
            assertNull(awaitItem())
            repository.emit(
                listOf(
                    Sermon(id = "s1", title = "First"),
                    Sermon(id = "s2", title = "Second")
                )
            )
            val sermon = awaitItem()
            assertEquals("s2", sermon?.id)
            assertEquals("Second", sermon?.title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
