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
            maven("https://www.jetbrains.com/intellij-repository/releases/")
            maven("https://repo.opencollab.dev/maven-releases/")
            maven("https://repo.opencollab.dev/maven-snapshots/")
            maven("https://storehouse.okaeri.eu/repository/maven-public/")
        }

        project.afterEvaluate { afterEvaluate(project, extension) }
    }

    private fun afterEvaluate(project: Project, extension: AllayExtension) {
        val dependency = if (!extension.apiOnly.get())
            "${Constant.DEPENDENCY_GROUP}:server:${extension.server.get()}"
        else "${Constant.DEPENDENCY_GROUP}:api:${extension.api.get()}"
        project.dependencies {
            add("compileOnly", dependency)
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
            pluginJar.set(shadowJarTask.flatMap { it.archiveFile })
            serverVersion.set(extension.server.get())
        }

        if (extension.descriptorInjection.get()) {
            descriptorInject(project, extension)
        }
    }

    private fun createShadowJarImplement(project: Project) = project.tasks.register("shadowJar", Jar::class.java) {
        group = Constant.TASK_GROUP
        archiveClassifier.set("shaded")
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
