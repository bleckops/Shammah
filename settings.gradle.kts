pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Shammah"
include(":androidApp")
include(":composeApp")
include(":webApp")
include(":core-domain")
include(":core-data")
include(":core-ui")
include(":feature-auth")
include(":feature-home")
include(":feature-events")
include(":feature-sermons")
include(":feature-resources")
include(":test-support")
 
