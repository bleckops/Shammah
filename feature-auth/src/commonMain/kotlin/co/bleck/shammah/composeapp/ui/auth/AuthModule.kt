package co.bleck.shammah.composeapp.ui.auth

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::AuthViewModel)
}
