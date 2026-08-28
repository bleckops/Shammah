package co.bleck.shammah.composeapp.ui.home.resources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.model.ResourceType
import co.bleck.shammah.domain.usecase.GetPublicResourcesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResourcesViewModel(
    private val getPublicResourcesUseCase: GetPublicResourcesUseCase
) : ViewModel() {

    private val _resources = MutableStateFlow<List<Resource>>(emptyList())
    val resources: StateFlow<List<Resource>> = _resources.asStateFlow()

    init {
        viewModelScope.launch {
            getPublicResourcesUseCase()
                .collectLatest { resources ->
                    _resources.value = resources
                }
        }
    }
}