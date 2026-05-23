package co.bleck.shammah.ui.home.sermons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.data.repository.SermonRepositoryImpl
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.GetSermonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SermonsViewModel : ViewModel() {
    private val getSermonsUseCase = GetSermonsUseCase(SermonRepositoryImpl())
    
    private val _sermons = MutableStateFlow<List<Sermon>>(emptyList())
    val sermons: StateFlow<List<Sermon>> = _sermons

    init {
        viewModelScope.launch {
            getSermonsUseCase().collectLatest {
                _sermons.value = it
            }
        }
    }
}