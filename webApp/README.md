# Web app (Compose Multiplatform / Wasm)

## Firebase setup

The web host loads the **official Firebase JS (compat)** SDK and a thin `firebaseBootstrap.js` bridge. Kotlin/Wasm repositories call `globalThis.ShammahFirebase`.

Android/iOS continue to use GitLive; only the **domain layer** is shared.

### Required config

Set the Firebase **Web** app values in root `local.properties` (or environment variables):

```properties
firebase.web.apiKey=...
firebase.web.appId=1:...:web:...
firebase.web.projectId=shammah-cf23e
firebase.web.authDomain=shammah-cf23e.firebaseapp.com
firebase.web.storageBucket=shammah-cf23e.appspot.com
firebase.web.messagingSenderId=...
```

Environment overrides: `FIREBASE_WEB_API_KEY`, `FIREBASE_WEB_APP_ID`, `FIREBASE_WEB_PROJECT_ID`, `FIREBASE_WEB_AUTH_DOMAIN`, `FIREBASE_WEB_STORAGE_BUCKET`, `FIREBASE_WEB_MESSAGING_SENDER_ID`.

`firebase.web.appId` is **required**. Other keys fall back to project defaults for `shammah-cf23e` when present.

Defaults / generated values are produced by the `generateWebFirebaseConfig` Gradle task in `:shared`.

### Console checklist

- Anonymous Auth enabled
- Localhost and deploy hostnames under Authorized domains
- Same Firestore rules as mobile for `banners`, `events`, `sermons`

### Run

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```
