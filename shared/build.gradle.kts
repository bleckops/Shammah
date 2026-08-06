import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

val generateWebFirebaseConfig by tasks.registering {
    val outputFile = layout.projectDirectory.file(
        "src/wasmJsMain/kotlin/co/bleck/shammah/data/firebase/WebFirebaseConfig.kt",
    )
    outputs.file(outputFile)
    inputs.file(rootProject.file("local.properties")).optional()

    doLast {
        val localProps = Properties()
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localFile.inputStream().use { localProps.load(it) }
        }

        fun prop(key: String, envKey: String, default: String): String {
            val fromProject = (project.findProperty(key) as? String)?.takeIf { it.isNotBlank() }
            val fromLocal = localProps.getProperty(key)?.takeIf { it.isNotBlank() }
            val fromEnv = System.getenv(envKey)?.takeIf { it.isNotBlank() }
            return fromProject ?: fromLocal ?: fromEnv ?: default
        }

        val projectId = prop("firebase.web.projectId", "FIREBASE_WEB_PROJECT_ID", "shammah-cf23e")
        val apiKey = prop(
            "firebase.web.apiKey",
            "FIREBASE_WEB_API_KEY",
            "AIzaSyAzOqHPi6vsrh4rcO7l4IjJIb7kTGFw96g",
        )
        val appId = prop("firebase.web.appId", "FIREBASE_WEB_APP_ID", "")
        val authDomain = prop(
            "firebase.web.authDomain",
            "FIREBASE_WEB_AUTH_DOMAIN",
            "$projectId.firebaseapp.com",
        )
        val storageBucket = prop(
            "firebase.web.storageBucket",
            "FIREBASE_WEB_STORAGE_BUCKET",
            "$projectId.appspot.com",
        )
        val messagingSenderId = prop(
            "firebase.web.messagingSenderId",
            "FIREBASE_WEB_MESSAGING_SENDER_ID",
            "",
        )

        fun escape(value: String): String =
            value.replace("\\", "\\\\").replace("\"", "\\\"")

        outputFile.asFile.writeText(
            """
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
                const val apiKey: String = "${escape(apiKey)}"
                const val appId: String = "${escape(appId)}"
                const val projectId: String = "${escape(projectId)}"
                const val authDomain: String = "${escape(authDomain)}"
                const val storageBucket: String = "${escape(storageBucket)}"
                const val messagingSenderId: String = "${escape(messagingSenderId)}"
            }
            """.trimIndent() + "\n",
        )
    }
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    android {
        namespace = "co.bleck.shammah.shared"
        compileSdk = 36
        minSdk = 26
        withHostTestBuilder {}
    }

    iosArm64()
    iosSimulatorArm64()
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
        }

        // Shared Firebase-backed repositories for Android + iOS (GitLive has no wasm artifacts).
        androidMain {
            kotlin.srcDir("src/mobileMain/kotlin")
            dependencies {
                // GitLive brings platform Firebase with empty versions; pin via BOM.
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation("com.google.firebase:firebase-common")
                implementation("com.google.firebase:firebase-auth")
                implementation("com.google.firebase:firebase-firestore")
                implementation(libs.gitlive.firebase.app)
                implementation(libs.gitlive.firebase.auth)
                implementation(libs.gitlive.firebase.firestore)
            }
        }

        iosMain {
            kotlin.srcDir("src/mobileMain/kotlin")
            dependencies {
                implementation(libs.gitlive.firebase.app)
                implementation(libs.gitlive.firebase.auth)
                implementation(libs.gitlive.firebase.firestore)
            }
        }

        // Web uses official Firebase JS (compat) via thin Wasm interop — no GitLive wasm.
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    if (name.contains("WasmJs", ignoreCase = true)) {
        dependsOn(generateWebFirebaseConfig)
    }
}
