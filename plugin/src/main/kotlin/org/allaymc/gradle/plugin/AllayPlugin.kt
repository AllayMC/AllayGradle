package org.allaymc.gradle.plugin

import org.allaymc.gradle.plugin.descriptor.descriptorInject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*

class AllayPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("allay", AllayExtension::class.java, project.objects)

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
        val isExtension = extension.isExtension.get()
        val version = extension.version.orNull ?: "master-SNAPSHOT"
        val apiOnly = extension.apiOnly.get()

        project.dependencies {
            val notation = if (isExtension || !apiOnly)
                "${Constant.DEPENDENCY_GROUP}:server:$version"
            else "${Constant.DEPENDENCY_GROUP}:api:$version"
            add("compileOnly", notation)
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
            this.version.set(version)
        }

        if (extension.descriptorInjection.get()) {
            descriptorInject(project, extension)
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
