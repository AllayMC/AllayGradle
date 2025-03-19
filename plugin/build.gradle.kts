plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

group = "org.allaymc.gradle"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(libs.kotlinx.serialization.json)
}

gradlePlugin {
    plugins {
        create("allayGradlePlugin") {
            id = libs.plugins.allay.gradle.get().pluginId
            version = libs.versions.allay.gradle.get()
            implementationClass = "org.allaymc.gradle.AllayPlugin"
        }
    }
    // We use `maven-publish` because jitpack expects us using this plugin,
    // so the publishing task in `java-gradle-plugin` should be disabled
    isAutomatedPublishing = false
}
