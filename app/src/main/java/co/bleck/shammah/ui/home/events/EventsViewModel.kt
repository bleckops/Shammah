package co.bleck.shammah.ui.home.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.usecase.FilterEventsForDateUseCase
import co.bleck.shammah.domain.usecase.GetEventsUseCase
import co.bleck.shammah.domain.usecase.ProjectEventDatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val filterEventsForDateUseCase: FilterEventsForDateUseCase,
    private val projectEventDatesUseCase: ProjectEventDatesUseCase
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val eventsForSelectedDate: StateFlow<List<Event>> =
        combine(_events, _selectedDate) { events, date ->
            filterEventsForDateUseCase(events, date)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val eventDates: StateFlow<Map<LocalDate, List<Event>>> =
        _events
            .map { projectEventDatesUseCase(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    init {
        viewModelScope.launch {
            getEventsUseCase().collectLatest { _events.value = it }
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
}
