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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LibertyFlow"
include(":app")
include("core")
include("feature")

include(":core:network")
include(":core:local")
include(":core:design-system")
include(":core:data")
include(":core:common")
include(":feature:home")
include(":feature:favorites")
include(":feature:collections")
include(":feature:more")
include(":feature:anime-details")
include(":feature:settings")
include(":feature:player")
include(":feature:info")
include(":feature:onboarding")
include(":shared-test")
