package co.bleck.shammah.ui.home.sermons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Sermon
import co.bleck.shammah.domain.usecase.GetSermonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SermonsViewModel @Inject constructor(
    private val getSermonsUseCase: GetSermonsUseCase
) : ViewModel() {

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
