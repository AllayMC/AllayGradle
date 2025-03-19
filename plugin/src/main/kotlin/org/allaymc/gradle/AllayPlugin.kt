package org.allaymc.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*

class AllayPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("allay", AllayExtension::class.java, project)

        project.repositories {
            mavenCentral()
            maven("https://jitpack.io/")
            maven("https://repo.opencollab.dev/maven-releases/")
            maven("https://repo.opencollab.dev/maven-snapshots/")
            maven("https://storehouse.okaeri.eu/repository/maven-public/")
        }

        project.afterEvaluate { afterEvaluate(project, extension) }
    }

    private fun afterEvaluate(project: Project, extension: AllayExtension) {
        val logger = project.logger
        val isExtension = extension.extension.get()
        val isUseServer = extension.useServer.get()

        val apiVersion = extension.version.orNull ?: extension.api.orNull ?: if (isExtension || isUseServer)
            extension.server.orNull ?: "master-SNAPSHOT"
        else "master-SNAPSHOT".also {
            logger.warn("Api version is not specified, defaulting to master-SNAPSHOT!")
        }

        val serverVersion = extension.version.orNull ?: extension.server.orNull ?: if (!isExtension && !isUseServer)
            apiVersion
        else "master-SNAPSHOT".also {
            logger.warn("Server version is not specified, defaulting to master-SNAPSHOT!")
        }

        project.dependencies {
            if (isExtension || isUseServer) add("compileOnly", "${Constant.DEPENDENCY_GROUP}:server:$serverVersion")
            else add("compileOnly", "${Constant.DEPENDENCY_GROUP}:api:$apiVersion")
        }

        val shadowJarTask = if (
            project.plugins.hasPlugin("com.gradleup.shadow")
            || project.plugins.hasPlugin("com.github.johnrengelman.shadow")
        ) {
            project.tasks.named("shadowJar", Jar::class.java)
        } else {
            createShadowJarImplement(project)
        }
        project.tasks.register<RunServerTask>("runServer") {
            group = Constant.TASK_GROUP
            dependsOn(shadowJarTask)
            shadowJar.set(shadowJarTask.flatMap { it.archiveFile })
            putExtension.set(isExtension)
            this.serverVersion.set(serverVersion)
        }
    }

    private fun createShadowJarImplement(project: Project) = project.tasks.register("shadowJar", Jar::class.java) {
        group = Constant.TASK_GROUP
        archiveClassifier.set("shadow")
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        from(sourceSets.getByName("main").output)
        val runtimeClasspath = project.configurations.getByName("runtimeClasspath")
        from({
            runtimeClasspath.filter { it.exists() }.map {
                if (it.isDirectory) it else project.zipTree(it)
            }
        })
    }
}
