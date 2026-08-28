plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.firebase.app.distribution)
}

if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
}

fun loadVersionProperty(key: String, default: String): String {
    val versionFile = rootProject.file("version.properties")
    if (!versionFile.exists()) return default
    return versionFile.readLines()
        .map { it.trim() }
        .firstOrNull { it.startsWith("$key=") && !it.startsWith("#") }
        ?.substringAfter("=", default)
        ?.trim()
        ?: default
}

val baseVersionName = loadVersionProperty("VERSION_NAME", "1.0.2")
val versionNameEnv = System.getenv("VERSION") ?: baseVersionName
val versionCodeEnv = System.getenv("VERSION_CODE")?.toIntOrNull() ?: 1

android {
    namespace = "co.bleck.shammah"
    compileSdk = 36

    defaultConfig {
        applicationId = "co.bleck.shammah"
        minSdk = 26
        targetSdk = 36
        versionCode = versionCodeEnv
        versionName = versionNameEnv
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("ANDROID_KEYSTORE_PATH")
            if (!keystorePath.isNullOrBlank()) {
                storeFile = rootProject.file(keystorePath)
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            val keystorePath = System.getenv("ANDROID_KEYSTORE_PATH")
            if (!keystorePath.isNullOrBlank()) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

}

dependencies {
    implementation(project(":composeApp"))
    implementation(project(":core-data"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}
