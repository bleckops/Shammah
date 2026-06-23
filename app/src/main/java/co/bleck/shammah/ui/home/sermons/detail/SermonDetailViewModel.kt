package co.bleck.shammah.ui.home.sermons.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.data.repository.SermonRepositoryImpl
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.GetSermonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SermonDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sermonId: String = checkNotNull(savedStateHandle["sermonId"]) {
        "sermonId navigation argument is required"
    }

    private val getSermonsUseCase = GetSermonsUseCase(SermonRepositoryImpl())

    private val _sermon = MutableStateFlow<Sermon?>(null)
    val sermon: StateFlow<Sermon?> = _sermon.asStateFlow()

    init {
        viewModelScope.launch {
            getSermonsUseCase().collectLatest { sermons ->
                _sermon.value = sermons.find { it.id == sermonId }
            }
        }
    }
}
