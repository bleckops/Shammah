@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package co.bleck.shammah.data.firebase

import co.bleck.shammah.data.firebase.js.ShammahFirebaseJs
import co.bleck.shammah.data.firebase.js.createFirebaseJsConfig
import kotlin.js.ExperimentalWasmJsInterop

actual object FirebaseBootstrap {
    private var initialized = false

    actual fun initialize() {
        if (initialized) return

        val appId = WebFirebaseConfig.appId
        if (appId.isBlank()) {
            error(
                "Missing firebase.web.appId. Set it in local.properties (or FIREBASE_WEB_APP_ID) " +
                    "to the Web app ID from the Firebase console.",
            )
        }

        val config = createFirebaseJsConfig(
            apiKey = WebFirebaseConfig.apiKey,
            authDomain = WebFirebaseConfig.authDomain,
            projectId = WebFirebaseConfig.projectId,
            storageBucket = WebFirebaseConfig.storageBucket,
            messagingSenderId = WebFirebaseConfig.messagingSenderId,
            appId = appId,
        )
        ShammahFirebaseJs.init(config)
        initialized = true
    }
}
