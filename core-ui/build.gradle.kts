import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

@OptIn(ExperimentalWasmDsl::class)
kotlin {
    android {
        namespace = "co.bleck.shammah.composeapp"
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
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material.icons.extended.multiplatform)
            implementation(libs.kotlinx.datetime)
        }
    }
}
