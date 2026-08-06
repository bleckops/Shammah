package co.bleck.shammah.data.firebase

/**
 * Public Firebase web client config for the same project as the Android app.
 *
 * Override via root `local.properties` / env and re-run:
 * `./gradlew :shared:generateWebFirebaseConfig` (rewrites this file).
 *
 * See also webApp/README.md.
 */
internal object WebFirebaseConfig {
    const val apiKey: String = "AIzaSyAzOqHPi6vsrh4rcO7l4IjJIb7kTGFw96g"
    const val appId: String = ""
    const val projectId: String = "shammah-cf23e"
    const val authDomain: String = "shammah-cf23e.firebaseapp.com"
    const val storageBucket: String = "shammah-cf23e.appspot.com"
    const val messagingSenderId: String = ""
}
