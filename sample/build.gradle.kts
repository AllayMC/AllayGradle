plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

group = "org.allaymc.gradle.sample"
version = libs.versions.allay.gradle.get()
description = "Test plugin of AllayGradle!"

allay {
    api = "0.17.0"
    plugin {
        name = "TestPlugin"
        entrance = ".TestPlugin"
        authors = listOf("Cdm2883", "daoge_cmd")
        website = "https://github.com/AllayMC/AllayGradle"
    }
}
