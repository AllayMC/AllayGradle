package org.allaymc.gradle.plugin

import org.allaymc.gradle.plugin.dsl.AllayDslExtension
import org.allaymc.gradle.plugin.tasks.GeneratePluginDescriptorTask
import org.allaymc.gradle.plugin.tasks.RunServerTask
import org.allaymc.gradle.plugin.tasks.ShadowJarTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.repositories

class AllayPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("allay", AllayDslExtension::class.java, project.objects)

        project.repositories {
            mavenCentral()
            maven("https://www.jetbrains.com/intellij-repository/releases/")
            maven("https://repo.opencollab.dev/maven-releases/")
            maven("https://repo.opencollab.dev/maven-snapshots/")
            maven("https://storehouse.okaeri.eu/repository/maven-public/") {
                content {
                    // Storehouse's repository returns http status code 500 instead of 404 for non-existent
                    // dependencies, which will cause the build to fail when using a dynamic version. To solve
                    // this problem, we explicitly indicate that there is no allay-api in this repository as
                    // well as allay-server
                    excludeGroup("org.allaymc.allay")
                }
            }
        }

        project.afterEvaluate { afterEvaluate(project, extension) }
    }

    private fun afterEvaluate(project: Project, extension: AllayDslExtension) {
        val api = extension.api.orNull
        val server = extension.server.orNull

        if ((api?.contains("SNAPSHOT", ignoreCase = true) == true) ||
            (server?.contains("SNAPSHOT", ignoreCase = true) == true)
        ) {
            project.repositories.maven("https://central.sonatype.com/repository/maven-snapshots/")
        }

        val dependency = if (!extension.apiOnly.get())
            "${Constants.DEPENDENCY_GROUP}:server:${server}"
        else "${Constants.DEPENDENCY_GROUP}:api:${api}"
        project.dependencies {
            add("compileOnly", dependency)
        }

        val shadowJarTask = if (
            project.plugins.hasPlugin("com.gradleup.shadow") ||
            project.plugins.hasPlugin("com.github.johnrengelman.shadow")
        ) {
            project.tasks.named("shadowJar", Jar::class.java)
        } else {
            project.tasks.register<ShadowJarTask>("shadowJar")
        }

        project.tasks.register<RunServerTask>("runServer") {
            dependsOn(shadowJarTask)
            pluginJar.set(shadowJarTask.flatMap { it.archiveFile })
            serverVersion.set(server)
        }

        if (extension.generatePluginDescriptor.get()) {
            val generatePluginDescriptorTask =
                project.tasks.register<GeneratePluginDescriptorTask>("generatePluginDescriptor") {
                    outputFile.set(project.layout.buildDirectory.file("resources/main/plugin.json"))

                    pluginEntrance.set(extension.plugin.entrance)
                    pluginName.set(extension.plugin.name)
                    pluginVersion.set(extension.plugin.version)
                    pluginAuthors.set(extension.plugin.authors)
                    pluginApiVersion.set(extension.plugin.apiVersion)
                    pluginDescription.set(extension.plugin.description)
                    pluginDependencies.set(extension.plugin.dependencies)
                    pluginWebsite.set(extension.plugin.website)

                    projectName.set(project.name)
                    projectVersion.set(project.version.toString())
                    projectDescription.set(project.description)
                }

            project.tasks.named("processResources") {
                dependsOn(generatePluginDescriptorTask)
            }
        }
    }
}
