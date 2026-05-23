package co.bleck.shammah.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.data.repository.BannerRepositoryImpl
import co.bleck.shammah.domain.model.Banner
import co.bleck.shammah.domain.usecase.GetBannersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val getBannersUseCase = GetBannersUseCase(BannerRepositoryImpl())

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
