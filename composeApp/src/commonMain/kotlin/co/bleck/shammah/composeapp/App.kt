package co.bleck.shammah.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import co.bleck.shammah.composeapp.di.appModules
import co.bleck.shammah.composeapp.platform.PlatformActions
import co.bleck.shammah.composeapp.platform.platformModule
import co.bleck.shammah.composeapp.ui.auth.AuthViewModel
import co.bleck.shammah.composeapp.ui.auth.WelcomeScreen
import co.bleck.shammah.composeapp.ui.home.MainScreen
import co.bleck.shammah.composeapp.ui.theme.ShammahTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication(application = {
        modules(*appModules())
    }) {
        ShammahTheme {
            val authViewModel: AuthViewModel = koinViewModel()
            val platformActions = koinInject<PlatformActions>()
            val currentUser by authViewModel.currentUser.collectAsState()

            if (currentUser != null) {
                MainScreen(
                    authViewModel = authViewModel,
                    onOpenUrl = platformActions::openUrl,
                    onOpenMaps = platformActions::openMaps,
                    onShareIcsCalendar = platformActions::shareIcsCalendar,
                    onShowMessage = platformActions::showMessage,
                    appVersionName = platformActions.appVersionName(),
                )
            } else {
                WelcomeScreen(authViewModel)
            }
        }
    }
}
