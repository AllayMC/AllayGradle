plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        create("allayGradle") {
            id = libs.plugins.allay.gradle.get().pluginId
            version = libs.versions.allay.gradle.get()
            implementationClass = "org.allaymc.gradle.AllayGradle"
        }
    }
}
