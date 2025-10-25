plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

group = "org.allaymc.gradle.sample"  // for entrance splicing

allay {
    api = "0.15.0"
    plugin {
        name = "TestPlugin"
        entrance = ".TestPlugin"  // "org.allaymc.gradle.sample.TestPlugin"
        description = "Test plugin of AllayGradle!"
        authors += "Cdm2883"
        version = libs.versions.allay.gradle
        website = "https://github.com/AllayMC/AllayGradle"
    }
}
