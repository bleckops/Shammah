import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    android {
        namespace = "co.bleck.shammah.domain"
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

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(project(":test-support"))
        }
    }
}
