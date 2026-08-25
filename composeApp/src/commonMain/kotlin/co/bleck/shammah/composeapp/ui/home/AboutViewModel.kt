package co.bleck.shammah.composeapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.bleck.shammah.domain.model.AboutContent
import co.bleck.shammah.domain.usecase.ObserveAboutContentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AboutViewModel(
    private val observeAboutContent: ObserveAboutContentUseCase,
) : ViewModel() {

    private val _content = MutableStateFlow(AboutContent())
    val content: StateFlow<AboutContent> = _content.asStateFlow()

    init {
        viewModelScope.launch {
            observeAboutContent().collectLatest { _content.value = it }
        }
    }
}
