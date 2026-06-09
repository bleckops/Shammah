package co.bleck.shammah.ui.home.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.data.repository.EventRepositoryImpl
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.model.EventType
import co.bleck.shammah.domain.usecase.GetEventsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class EventsViewModel : ViewModel() {

    private val getEventsUseCase = GetEventsUseCase(EventRepositoryImpl())

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    /** Events that fall on the currently selected day.
     *  Birthdays match by month+day only (they recur every year). */
    val eventsForSelectedDate: StateFlow<List<Event>> =
        combine(_events, _selectedDate) { events, date ->
            events.filter { event ->
                val eventDate = event.date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                if (event.type == EventType.birthdays) {
                    // Ignore year — match any year's same month+day
                    eventDate.monthValue == date.monthValue &&
                        eventDate.dayOfMonth == date.dayOfMonth
                } else {
                    eventDate == date
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** All events grouped by date — used to render calendar dots.
     *  Birthdays are projected across the visible calendar range (±2 years)
     *  so their dot appears on the correct month/day every year. */
    val eventDates: StateFlow<Map<LocalDate, List<Event>>> =
        _events
            .map { list ->
                val today = LocalDate.now()
                val result = mutableMapOf<LocalDate, MutableList<Event>>()
                list.forEach { event ->
                    val rawDate = event.date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    if (event.type == EventType.birthdays) {
                        // Repeat the birthday on the same month/day for -1 .. +2 years
                        for (yearOffset in -1..2) {
                            val key = rawDate.withYear(today.year + yearOffset)
                            result.getOrPut(key) { mutableListOf() }.add(event)
                        }
                    } else {
                        result.getOrPut(rawDate) { mutableListOf() }.add(event)
                    }
                }
                result
            }
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
