package co.bleck.shammah.composeapp.ui.home

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AboutViewModel)
}
