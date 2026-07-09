package co.bleck.shammah.ui.home.events

import app.cash.turbine.test
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.usecase.FilterEventsForDateUseCase
import co.bleck.shammah.domain.usecase.GetEventsUseCase
import co.bleck.shammah.domain.usecase.ProjectEventDatesUseCase
import co.bleck.shammah.fake.FakeEventRepository
import co.bleck.shammah.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class EventsViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private lateinit var repository: FakeEventRepository
    private lateinit var viewModel: EventsViewModel

    @Before
    fun setUp() {
        repository = FakeEventRepository()
        viewModel = EventsViewModel(
            getEventsUseCase = GetEventsUseCase(repository),
            filterEventsForDateUseCase = FilterEventsForDateUseCase(),
            projectEventDatesUseCase = ProjectEventDatesUseCase()
        )
    }

    private fun dateAt(year: Int, month: Int, day: Int): Date =
        Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant())

    @Test
    fun `loads events from use case`() = runTest {
        val events = listOf(Event(id = "e1", title = "Camp", date = dateAt(2025, 8, 1)))

        viewModel.events.test {
            assertEquals(emptyList<Event>(), awaitItem())
            repository.emit(events)
            assertEquals(events, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selectDate updates selected date`() = runTest {
        val target = LocalDate.of(2025, 8, 1)

        viewModel.selectDate(target)

        assertEquals(target, viewModel.selectedDate.value)
    }
}
