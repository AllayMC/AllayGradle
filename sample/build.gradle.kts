plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

group = "org.allaymc.gradle.sample"

allay {
    version = "0.2.0"
    plugin {
        description = "Test plugin of AllayGradle!"
        authors += "Cdm2883"
        version = libs.versions.allay.gradle
        website = "AllayMC/AllayGradle"
    }
}
