plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

group = "org.allaymc.gradle.sample"

allay {
    version = "0.1.2"
    plugin {
        description = "Test plugin of AllayGradle!"
        authors += "MineBuilder"
        version = libs.versions.allay.gradle
        website = "MineBuilders/AllayGradle"
    }
}
