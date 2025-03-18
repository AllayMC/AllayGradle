plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
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
        create("allayGradlePlugin") {
            id = libs.plugins.allay.gradle.get().pluginId
            version = libs.versions.allay.gradle.get()
            implementationClass = "org.allaymc.gradle.AllayPlugin"
        }
    }
}
