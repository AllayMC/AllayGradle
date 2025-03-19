plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

allay {
    version = "0.2.0"
    plugin {
        name = "TestPlugin"
        entrance = ".TestPlugin"
        description = "Test plugin of AllayGradle!"
        authors += "Cdm2883"
        version = libs.versions.allay.gradle
        website = "https://github.com/AllayMC/AllayGradle"
    }
}

tasks.publish {
    enabled = false
}