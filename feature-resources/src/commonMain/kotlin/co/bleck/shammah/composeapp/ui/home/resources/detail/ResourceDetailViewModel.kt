package co.bleck.shammah.composeapp.ui.home.resources.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.usecase.ObserveResourceByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResourceDetailViewModel(
    resourceId: String,
    private val observeResourceByIdUseCase: ObserveResourceByIdUseCase
) : ViewModel() {
    private val _resource = MutableStateFlow<Resource?>(null)
    val resource: StateFlow<Resource?> = _resource.asStateFlow()

    init {
        viewModelScope.launch {
            observeResourceByIdUseCase(resourceId).collect { _resource.value = it }
        }
    }
}
