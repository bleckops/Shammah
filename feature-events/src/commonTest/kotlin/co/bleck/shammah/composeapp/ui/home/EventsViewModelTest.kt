@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.events

import app.cash.turbine.test
import co.bleck.shammah.testsupport.FakeEventRepository
import co.bleck.shammah.testsupport.MainDispatcherTest
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.usecase.FilterEventsForDateUseCase
import co.bleck.shammah.domain.usecase.GetEventsUseCase
import co.bleck.shammah.domain.usecase.ProjectEventDatesUseCase
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EventsViewModelTest : MainDispatcherTest() {
    private lateinit var repository: FakeEventRepository
    private lateinit var viewModel: EventsViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()
        repository = FakeEventRepository()
        viewModel = EventsViewModel(
            getEventsUseCase = GetEventsUseCase(repository),
            filterEventsForDateUseCase = FilterEventsForDateUseCase(TimeZone.UTC),
            projectEventDatesUseCase = ProjectEventDatesUseCase(TimeZone.UTC),
        )
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
    }

    private fun dateAt(year: Int, month: Int, day: Int) =
        LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC)

    @Test
    fun loadsEventsFromUseCase() = runTest {
        val events = listOf(Event(id = "e1", title = "Camp", date = dateAt(2025, 8, 1)))

        viewModel.events.test {
            assertEquals(emptyList(), awaitItem())
            repository.emit(events)
            assertEquals(events, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectDateUpdatesSelectedDate() = runTest {
        val target = LocalDate(2025, 8, 1)
        viewModel.selectDate(target)
        assertEquals(target, viewModel.selectedDate.value)
    }
}
