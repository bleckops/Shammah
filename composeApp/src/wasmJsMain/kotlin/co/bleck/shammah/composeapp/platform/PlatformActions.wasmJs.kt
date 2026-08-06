package co.bleck.shammah.composeapp.platform

import kotlinx.browser.window
import org.koin.core.module.Module
import org.koin.dsl.module

actual class PlatformActions {
    actual fun openUrl(url: String) {
        window.open(url, "_blank")
    }

    actual fun openMaps(latitude: Double, longitude: Double, query: String) {
        val normalizedQuery = query.replace(" ", "+")
        openUrl("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude+$normalizedQuery")
    }

    actual fun shareIcsCalendar(filename: String, icsContent: String) {
        // Keep web share simple and robust across wasm browser targets.
        window.alert("Calendario generado: $filename")
    }

    actual fun showMessage(message: String) {
        window.alert(message)
    }

    actual fun appVersionName(): String = "web"
}

actual fun platformModule(): Module = module {
    single { PlatformActions() }
}
