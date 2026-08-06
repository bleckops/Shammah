# Web app (Compose Multiplatform / Wasm)

## Firebase setup

The web host loads the **official Firebase JS (compat)** SDK and a thin `firebaseBootstrap.js` bridge. Kotlin/Wasm repositories call `globalThis.ShammahFirebase`.

Android/iOS continue to use GitLive; only the **domain layer** is shared.

### Required config (do not commit keys)

`WebFirebaseConfig.kt` in git is intentionally empty. Put secrets in **gitignored** `local.properties` (repo root) or environment variables, then generate the Kotlin file locally.

#### 1. Get values from Firebase Console

1. Open [Firebase Console](https://console.firebase.google.com/) → your project  
2. Project settings (gear) → **Your apps** → Web app (`</>`), or add a Web app  
3. Copy the config object: `apiKey`, `authDomain`, `projectId`, `storageBucket`, `messagingSenderId`, `appId`

#### 2. Write root `local.properties`

```properties
# already used by Android SDK
sdk.dir=/path/to/Android/sdk

# Firebase Web (from console web app config)
firebase.web.apiKey=AIza...
firebase.web.appId=1:123456789:web:abcdef
firebase.web.projectId=your-project-id
firebase.web.authDomain=your-project-id.firebaseapp.com
firebase.web.storageBucket=your-project-id.appspot.com
firebase.web.messagingSenderId=123456789
```

`local.properties` is already in `.gitignore` — never commit it.

Equivalent env vars for CI:

| local.properties | Environment |
| --- | --- |
| `firebase.web.apiKey` | `FIREBASE_WEB_API_KEY` |
| `firebase.web.appId` | `FIREBASE_WEB_APP_ID` |
| `firebase.web.projectId` | `FIREBASE_WEB_PROJECT_ID` |
| `firebase.web.authDomain` | `FIREBASE_WEB_AUTH_DOMAIN` |
| `firebase.web.storageBucket` | `FIREBASE_WEB_STORAGE_BUCKET` |
| `firebase.web.messagingSenderId` | `FIREBASE_WEB_MESSAGING_SENDER_ID` |

If `authDomain` / `storageBucket` are omitted, Gradle derives them from `projectId` when generating.

#### 3. Generate config into Kotlin

```bash
./gradlew :shared:generateWebFirebaseConfig
```

That rewrites `shared/src/wasmJsMain/.../WebFirebaseConfig.kt` for **local builds**.  
Wasm compile tasks depend on this, so a normal web build regenerates automatically when you have the properties set.

**Do not commit** a generated file that contains real keys. After generating for a local run, either leave the file unstaged, or re-run generate with empty props / leave empty values before committing.

#### 4. Run the web app

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

### Console checklist

- Anonymous Auth enabled
- Localhost and deploy hostnames under Authorized domains
- Same Firestore rules as mobile for `banners`, `events`, `sermons`

### Note on “public” API keys

Firebase Web API keys are client-side by design; security is **Auth + Firestore rules + App Check**, not key secrecy. Still, keep repo free of project keys so you can rotate without force-rewriting history.
