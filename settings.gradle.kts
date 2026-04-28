pluginManagement {
    repositories {
        google()
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

rootProject.name = "Taxi"
include(":app")
include(":feature:taxi_travel_options:data")
include(":feature:taxi_travel_options:domain")
include(":feature:taxi_travel_options:ui")
include(":feature:taxi_travel_available_riders:data")
include(":feature:taxi_travel_available_riders:domain")
include(":feature:taxi_travel_available_riders:ui")
include(":core:network")
include(":core:common")
include(":core:feature")