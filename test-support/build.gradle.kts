import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    android {
        namespace = "co.bleck.shammah.testsupport"
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
            implementation(project(":core-domain"))
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.datetime)
        }
    }
}
