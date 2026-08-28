package co.bleck.shammah.composeapp.ui.home.resources

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val resourcesModule = module {
    viewModelOf(::ResourcesViewModel)
}
