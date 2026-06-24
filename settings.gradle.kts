pluginManagement {
    includeBuild("build-logic")
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "LibertyFlow"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// App
include(":app")

// Core
include(":core")
include(":core:network")
include(":core:preferences")
include(":core:local-dbs")
include(":core:domain")
include(":core:data")
include(":core:design-system")
include(":core:common")

// Features
include(":feature")
include(":feature:home")
include(":feature:favorites")
include(":feature:collections")
include(":feature:more")
include(":feature:settings")
include(":feature:anime-details")
include(":feature:player")
include(":feature:common")
include(":feature:information")
