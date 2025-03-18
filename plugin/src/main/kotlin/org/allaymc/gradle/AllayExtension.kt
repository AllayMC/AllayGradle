package org.allaymc.gradle

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class AllayExtension @Inject constructor(
    objects: ObjectFactory,
    @Suppress("UNUSED_PARAMETER") project: Project
) {
    val extension: Property<Boolean> = objects.property(Boolean::class.java).convention(false)

    val useServer: Property<Boolean> = objects.property(Boolean::class.java).convention(false)
    val api: Property<String> = objects.property(String::class.java)
    val server: Property<String> = objects.property(String::class.java)
}
