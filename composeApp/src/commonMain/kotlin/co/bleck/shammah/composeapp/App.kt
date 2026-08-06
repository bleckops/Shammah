package co.bleck.shammah.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import co.bleck.shammah.composeapp.di.composeModule
import co.bleck.shammah.composeapp.platform.platformModule
import co.bleck.shammah.composeapp.ui.auth.AuthViewModel
import co.bleck.shammah.composeapp.ui.auth.WelcomeScreen
import co.bleck.shammah.composeapp.ui.home.MainScreen
import co.bleck.shammah.composeapp.ui.theme.ShammahTheme
import co.bleck.shammah.di.sharedModule
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication(application = {
        modules(sharedModule, composeModule, platformModule())
    }) {
        ShammahTheme {
            val authViewModel: AuthViewModel = koinViewModel()
            val currentUser by authViewModel.currentUser.collectAsState()

            if (currentUser != null) {
                MainScreen(authViewModel)
            } else {
                WelcomeScreen(authViewModel)
            }
        }
    }
}
