@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.events.detail

import app.cash.turbine.test
import co.bleck.shammah.composeapp.fake.FakeEventRepository
import co.bleck.shammah.composeapp.util.MainDispatcherTest
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import co.bleck.shammah.domain.usecase.ObserveEventByIdUseCase
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EventDetailViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeEventRepository
    private lateinit var viewModel: EventDetailViewModel

    override fun setUp() {
        super.setUp()
        repository = FakeEventRepository()
        viewModel = EventDetailViewModel(
            eventId = "e2",
            observeEventByIdUseCase = ObserveEventByIdUseCase(repository),
        )
    }

    private fun dateAt(year: Int, month: Int, day: Int) =
        LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC)

    @Test
    fun loadsEventMatchingNavigationId() = runTest {
        viewModel.event.test {
            assertNull(awaitItem())
            repository.emit(
                listOf(
                    Event(id = "e1", title = "First",  type = EventType.prayer, date = dateAt(2025, 8, 1)),
                    Event(id = "e2", title = "Second", type = EventType.social, date = dateAt(2025, 9, 5)),
                ),
            )
            val event = awaitItem()
            assertEquals("e2", event?.id)
            assertEquals("Second", event?.title)
            assertEquals(EventType.social, event?.type)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
