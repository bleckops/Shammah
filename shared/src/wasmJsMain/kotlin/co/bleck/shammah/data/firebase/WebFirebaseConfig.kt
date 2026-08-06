package co.bleck.shammah.data.firebase

/**
 * Public Firebase web client config for the same project as the Android app.
 *
 * Values come from root `local.properties` / env via:
 * `./gradlew :shared:generateWebFirebaseConfig` (rewrites this file).
 *
 * Do **not** commit real API keys here — keep them only in local.properties or CI secrets.
 * See also webApp/README.md.
 */
internal object WebFirebaseConfig {
    const val apiKey: String = ""
    const val appId: String = ""
    const val projectId: String = ""
    const val authDomain: String = ""
    const val storageBucket: String = ""
    const val messagingSenderId: String = ""
}
