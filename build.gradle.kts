plugins {
    id("java-library")
    id("maven-publish")
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlin.jvm) apply false
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "org.allaymc.gradle"

    publishing {
        repositories {
            // Jitpack requires us to publish artifacts to local maven repo
            mavenLocal()
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
}