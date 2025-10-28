plugins {
    `kotlin-dsl`
    alias(libs.plugins.gradle.plugin.publish)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

group = "org.allaymc"
version = libs.versions.allay.gradle.get()

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(libs.kotlinx.serialization.json)
}

gradlePlugin {
    website = "https://github.com/AllayMC/AllayGradle"
    vcsUrl = "https://github.com/AllayMC/AllayGradle.git"
    plugins {
        create("allayGradlePlugin") {
            id = libs.plugins.allay.gradle.get().pluginId
            displayName = "Gradle plugin for AllayMC"
            description = "A Gradle plugin designed to boost Allay plugin development!"
            tags = listOf("minecraft", "allaymc", "minecraft-plugin", "bedrock")
            implementationClass = "org.allaymc.gradle.plugin.AllayPlugin"
        }
    }
}
