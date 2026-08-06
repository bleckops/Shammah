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
    const val apiKey: String = "AIzaSyC0nDV_OFmm9oKd5mfGOEGneHsgatTr434"
    const val appId: String = "1:899239260269:web:f1150c6690c577057d8f64"
    const val projectId: String = "shammah-cf23e"
    const val authDomain: String = "shammah-cf23e.firebaseapp.com"
    const val storageBucket: String = "shammah-cf23e.firebasestorage.app"
    const val messagingSenderId: String = "899239260269"
}
