package org.allaymc.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class AllayExtension @Inject constructor(project: Project) {
    val extension: Property<Boolean> = project.objects.property(Boolean::class.java).convention(false)

    val useServer: Property<Boolean> = project.objects.property(Boolean::class.java).convention(false)
    val version: Property<String> = project.objects.property(String::class.java)
    val api: Property<String> = project.objects.property(String::class.java)
    val server: Property<String> = project.objects.property(String::class.java)
}
