package co.bleck.shammah.composeapp.ui.home.events

import co.bleck.shammah.composeapp.ui.home.events.detail.EventDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val eventsModule = module {
    viewModelOf(::EventsViewModel)
    viewModel { (eventId: String) -> EventDetailViewModel(eventId, get()) }
}
