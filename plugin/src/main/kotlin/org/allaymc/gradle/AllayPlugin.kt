package org.allaymc.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

class AllayPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val logger = project.logger
        val extension = project.extensions.create("allay", AllayExtension::class.java, project)

        project.repositories {
            mavenCentral()
            maven("https://jitpack.io/")
            maven("https://repo.opencollab.dev/maven-releases/")
            maven("https://repo.opencollab.dev/maven-snapshots/")
            maven("https://storehouse.okaeri.eu/repository/maven-public/")
        }

        project.afterEvaluate {
            val isExtension = extension.extension.get()
            val isUseServer = extension.useServer.get()

            val apiVersion = extension.api.orNull ?: if (isExtension || isUseServer)
                extension.server.orNull ?: "master-SNAPSHOT"
            else "master-SNAPSHOT".also {
                logger.warn("Api version is not specified, defaulting to master-SNAPSHOT!")
            }

            val serverVersion = extension.server.orNull ?: if (!isExtension && !isUseServer)
                apiVersion
            else "master-SNAPSHOT".also {
                logger.warn("Server version is not specified, defaulting to master-SNAPSHOT!")
            }

            project.dependencies {
                if (isExtension || isUseServer) add("compileOnly", "org.allaymc.allay:server:$serverVersion")
                else add("compileOnly", "org.allaymc.allay:api:$apiVersion")
            }
        }
    }
}
