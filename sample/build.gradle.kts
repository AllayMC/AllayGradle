plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

// for entrance splicing
group = "org.allaymc.gradle.sample"
version = libs.versions.allay.gradle.get()
description = "Test plugin of AllayGradle!"

allay {
    api = "0.15.0"
    plugin {
        name = "TestPlugin"
        // "org.allaymc.gradle.sample.TestPlugin"
        entrance = ".TestPlugin"
        api = ">= 0.15.0"
        authors += "Cdm2883"
        website = "https://github.com/AllayMC/AllayGradle"
    }
}
