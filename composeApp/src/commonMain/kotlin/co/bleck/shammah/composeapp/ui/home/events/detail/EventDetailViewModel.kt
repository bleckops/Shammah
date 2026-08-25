@file:OptIn(kotlin.time.ExperimentalTime::class)

package co.bleck.shammah.composeapp.ui.home.events.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Event
import co.bleck.shammah.domain.usecase.ObserveEventByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventDetailViewModel(
    eventId: String,
    private val observeEventByIdUseCase: ObserveEventByIdUseCase
) : ViewModel() {

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event.asStateFlow()

    init {
        viewModelScope.launch {
            observeEventByIdUseCase(eventId).collectLatest { event ->
                _event.value = event
            }
        }
    }
}
