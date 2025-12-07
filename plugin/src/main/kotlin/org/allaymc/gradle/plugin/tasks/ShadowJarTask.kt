package org.allaymc.gradle.plugin.tasks

import org.allaymc.gradle.plugin.Constants
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar

abstract class ShadowJarTask : Jar() {
    init {
        group = Constants.TASK_GROUP
        archiveClassifier.set("shaded")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
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