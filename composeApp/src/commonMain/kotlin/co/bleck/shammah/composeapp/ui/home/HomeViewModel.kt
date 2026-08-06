package co.bleck.shammah.composeapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.usecase.GetBannersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getBannersUseCase: GetBannersUseCase
) : ViewModel() {

    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners: StateFlow<List<Banner>> = _banners

    init {
        viewModelScope.launch {
            getBannersUseCase().collectLatest {
                _banners.value = it
            }
        }
    }
}
