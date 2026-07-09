package co.bleck.shammah.ui.home.sermons.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.ObserveSermonByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SermonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeSermonByIdUseCase: ObserveSermonByIdUseCase
) : ViewModel() {
    private val sermonId: String = checkNotNull(savedStateHandle["sermonId"]) {
        "sermonId navigation argument is required"
    }

    private val _sermon = MutableStateFlow<Sermon?>(null)
    val sermon: StateFlow<Sermon?> = _sermon.asStateFlow()

    init {
        viewModelScope.launch {
            observeSermonByIdUseCase(sermonId).collectLatest { sermon ->
                _sermon.value = sermon
            }
        }
    }
}
