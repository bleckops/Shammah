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
            implementation(project(":core-domain"))
            implementation(project(":core-ui"))
            implementation(project(":feature-auth"))
            implementation(project(":feature-events"))
            implementation(project(":feature-sermons"))
            implementation(project(":feature-resources"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.material.icons.extended.multiplatform)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor3)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(project(":test-support"))
        }
    }
}
