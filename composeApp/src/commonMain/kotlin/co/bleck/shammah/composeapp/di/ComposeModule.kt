package co.bleck.shammah.composeapp.di

import co.bleck.shammah.composeapp.ui.auth.AuthViewModel
import co.bleck.shammah.composeapp.ui.home.AboutViewModel
import co.bleck.shammah.composeapp.ui.home.HomeViewModel
import co.bleck.shammah.composeapp.ui.home.events.EventsViewModel
import co.bleck.shammah.composeapp.ui.home.events.detail.EventDetailViewModel
import co.bleck.shammah.composeapp.ui.home.sermons.SermonsViewModel
import co.bleck.shammah.composeapp.ui.home.sermons.detail.SermonDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val composeModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AboutViewModel)
    viewModelOf(::SermonsViewModel)
    viewModelOf(::EventsViewModel)
    viewModel { (sermonId: String) -> SermonDetailViewModel(sermonId, get()) }
    viewModel { (eventId: String) -> EventDetailViewModel(eventId, get()) }
}
