package co.bleck.shammah.composeapp.ui.home.sermons

import co.bleck.shammah.composeapp.ui.home.sermons.detail.SermonDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sermonsModule = module {
    viewModelOf(::SermonsViewModel)
    viewModel { (sermonId: String) -> SermonDetailViewModel(sermonId, get()) }
}
