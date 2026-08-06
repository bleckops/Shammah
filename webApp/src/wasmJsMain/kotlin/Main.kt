package co.bleck.shammah.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import co.bleck.shammah.composeapp.App
import co.bleck.shammah.data.firebase.FirebaseBootstrap
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    FirebaseBootstrap.initialize()
    ComposeViewport(document.body!!) {
        App()
    }
}
