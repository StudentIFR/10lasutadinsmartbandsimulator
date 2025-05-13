pluginManagement {
    repositories {
        // Permite rezolvarea plugin-urilor Google (Android Gradle, Hilt, etc.)
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Pentru alte plugin-uri Kotlin, Gradle etc.
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Împiedică repo-uri ad hoc în sub-module
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")   // << adaugă asta
    }
}

rootProject.name = "SmartBandSimulator"
include(":app")
