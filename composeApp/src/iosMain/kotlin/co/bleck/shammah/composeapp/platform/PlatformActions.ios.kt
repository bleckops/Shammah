package co.bleck.shammah.composeapp.platform

import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class PlatformActions {
    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }

    actual fun openMaps(latitude: Double, longitude: Double, query: String) {
        openUrl("http://maps.apple.com/?ll=$latitude,$longitude&q=$query")
    }

    actual fun shareIcsCalendar(filename: String, icsContent: String) {
        // iOS share-sheet implementation is injected from the host app.
    }

    actual fun showMessage(message: String) {
        // No-op placeholder for iOS.
    }

    actual fun appVersionName(): String =
        (NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String) ?: "1.0"
}

actual fun platformModule(): Module = module {
    single { PlatformActions() }
}
