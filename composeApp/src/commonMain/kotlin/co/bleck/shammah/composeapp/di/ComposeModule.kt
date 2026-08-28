package co.bleck.shammah.composeapp.di

import co.bleck.shammah.composeapp.platform.platformModule
import co.bleck.shammah.composeapp.ui.auth.authModule
import co.bleck.shammah.composeapp.ui.home.events.eventsModule
import co.bleck.shammah.composeapp.ui.home.homeModule
import co.bleck.shammah.composeapp.ui.home.resources.resourcesModule
import co.bleck.shammah.composeapp.ui.home.sermons.sermonsModule
import co.bleck.shammah.di.dataModule
import co.bleck.shammah.di.domainModule
import org.koin.core.module.Module

fun appModules(): Array<Module> = arrayOf(
    domainModule,
    dataModule,
    authModule,
    homeModule,
    eventsModule,
    sermonsModule,
    resourcesModule,
    platformModule(),
)
