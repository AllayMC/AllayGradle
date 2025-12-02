package org.allaymc.gradle.plugin.dsl

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

@AllayDslMarker
abstract class AllayDslExtension @Inject constructor(objects: ObjectFactory) {
    val api: Property<String> = objects.property(String::class.java)
    val server: Property<String> = objects.property(String::class.java).convention("+")
    val apiOnly: Property<Boolean> = objects.property(Boolean::class.java).convention(true)

    val generatePluginDescriptor: Property<Boolean> = objects.property(Boolean::class.java).convention(true)

    val plugin: PluginDsl = objects.newInstance(PluginDsl::class.java)
    fun plugin(action: PluginDsl.() -> Unit) = plugin.action()
}