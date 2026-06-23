package co.bleck.shammah.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import co.bleck.shammah.ui.auth.AuthViewModel
import co.bleck.shammah.ui.auth.WelcomeScreen
import co.bleck.shammah.ui.home.MainScreen
import co.bleck.shammah.ui.theme.ShammahTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShammahTheme {
                val authViewModel: AuthViewModel = viewModel()
                val currentUser by authViewModel.currentUser.collectAsState()

                if (currentUser != null) {
                    MainScreen(authViewModel)
                } else {
                    WelcomeScreen(authViewModel)
                }
            }
        }
    }
}
