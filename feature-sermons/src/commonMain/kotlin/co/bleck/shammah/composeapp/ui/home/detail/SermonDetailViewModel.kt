package co.bleck.shammah.composeapp.ui.home.sermons.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.ObserveSermonByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SermonDetailViewModel(
    sermonId: String,
    private val observeSermonByIdUseCase: ObserveSermonByIdUseCase
) : ViewModel() {

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
