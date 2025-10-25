rootProject.name = "AllayGradle"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

includeBuild("plugin")
include(":samples:test-plugin")
include(":samples:test-extension")
