plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
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
            implementationClass = "org.allaymc.gradle.AllayPlugin"
        }
    }
}

publishing {
    repositories {
        // Jitpack requires us to publish artifacts to local maven repo
        mavenLocal()
    }

    java {
        withSourcesJar()
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                inceptionYear.set("2025")
                packaging = "jar"
                url.set("https://github.com/AllayMC/AllayGradle")

                scm {
                    connection.set("scm:git:git://github.com/AllayMC/AllayGradle.git")
                    developerConnection.set("scm:git:ssh://github.com/AllayMC/AllayGradle.git")
                    url.set("https://github.com/AllayMC/AllayGradle")
                }

                licenses {
                    license {
                        name.set("LGPL 3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.en.html")
                    }
                }

                developers {
                    developer {
                        name.set("AllayMC Team")
                        organization.set("AllayMC")
                        organizationUrl.set("https://github.com/AllayMC")
                    }
                }
            }
        }
    }
}
