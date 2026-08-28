package co.bleck.shammah.composeapp.ui.home.resources

import co.bleck.shammah.composeapp.ui.home.resources.detail.ResourceDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val resourcesModule = module {
    viewModelOf(::ResourcesViewModel)
    viewModel { (resourceId: String) -> ResourceDetailViewModel(resourceId, get()) }
}
