plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

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
            implementationClass = "org.allaymc.gradle.plugin.AllayPlugin"
        }
    }
}
