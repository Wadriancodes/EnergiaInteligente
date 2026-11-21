// Sobrescreve as variáveis do Windows em tempo real para o Android Studio
System.setProperty("android.prefs.root", "C:/Users/WINDOWS/.android")
System.setProperty("android.overridePathCheck", "true")
System.clearProperty("ANDROID_PREFS_ROOT")

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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Energia Inteligente"
include(":app")
 