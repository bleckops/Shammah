@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package co.bleck.shammah.data.firebase

import co.bleck.shammah.data.firebase.js.ShammahFirebaseJs
import co.bleck.shammah.data.firebase.js.createFirebaseJsConfig
import kotlin.js.ExperimentalWasmJsInterop

actual object FirebaseBootstrap {
    private var initialized = false

    actual fun initialize() {
        if (initialized) return

        val apiKey = WebFirebaseConfig.apiKey
        val appId = WebFirebaseConfig.appId
        val projectId = WebFirebaseConfig.projectId
        if (apiKey.isBlank() || appId.isBlank() || projectId.isBlank()) {
            error(
                "Missing Firebase web config. Set firebase.web.apiKey, firebase.web.appId, and " +
                    "firebase.web.projectId in local.properties (or FIREBASE_WEB_* env vars), " +
                    "then run ./gradlew :shared:generateWebFirebaseConfig. See webApp/README.md.",
            )
        }

        val config = createFirebaseJsConfig(
            apiKey = apiKey,
            authDomain = WebFirebaseConfig.authDomain.ifBlank { "$projectId.firebaseapp.com" },
            projectId = projectId,
            storageBucket = WebFirebaseConfig.storageBucket.ifBlank { "$projectId.appspot.com" },
            messagingSenderId = WebFirebaseConfig.messagingSenderId,
            appId = appId,
        )
        ShammahFirebaseJs.init(config)
        initialized = true
    }
}
