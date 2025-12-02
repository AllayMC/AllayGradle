package org.allaymc.gradle.plugin.dsl

import org.allaymc.gradle.plugin.data.PluginDependency
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

@AllayDslMarker
abstract class PluginDsl @Inject constructor(objects: ObjectFactory) {
    val entrance: Property<String> = objects.property(String::class.java)
    val name: Property<String> = objects.property(String::class.java)
    val version: Property<String> = objects.property(String::class.java)

    val authors: ListProperty<String> = objects.listProperty(String::class.java).convention(emptyList())
    fun authors(vararg names: String) = authors.addAll(names.toList())

    /* Optional Fields */

    val apiVersion: Property<String> = objects.property(String::class.java)

    /** Alias for [apiVersion]. */
    val api = apiVersion

    val description: Property<String> = objects.property(String::class.java)

    val dependencies: ListProperty<PluginDependency> = objects.listProperty(PluginDependency::class.java)
    fun dependencies(vararg plugins: PluginDependency) = dependencies.addAll(plugins.toList())
    fun dependency(name: String, version: String? = null, optional: Boolean = false) =
        dependencies.add(PluginDependency(name, version, optional))

    val website: Property<String> = objects.property(String::class.java)

    operator fun <T : Any> ListProperty<T>.plusAssign(item: T) = addAll(item)
}