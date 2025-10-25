plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

allay {
    api = "0.15.0"
    plugin {
        name = "TestPlugin"
        entrance = "org.allaymc.gradle.sample.TestPlugin"
        description = "Test plugin of AllayGradle!"
        authors += "Cdm2883"
        version = libs.versions.allay.gradle
        website = "https://github.com/AllayMC/AllayGradle"
    }
}
