package org.allaymc.gradle.plugin

import org.allaymc.gradle.plugin.descriptor.PluginDescriptor
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.mapProperty
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate", "unused")
@ExtensionMarker
abstract class AllayExtension @Inject constructor(objects: ObjectFactory) {
    val api: Property<String?> = objects.property(String::class.java)
    val server: Property<String> = objects.property(String::class.java).convention("+")
    val apiOnly: Property<Boolean> = objects.property(Boolean::class.java).convention(true)

    val descriptorInjection: Property<Boolean> = objects.property(Boolean::class.java).convention(true)
    var entrance: String?
        get() = plugin.entrance.orNull
        set(value) = plugin.entrance.set(value)

    val plugin: Plugin = objects.newInstance(Plugin::class.java)
    fun plugin(action: Plugin.() -> Unit) = plugin.action()

    abstract class Config(objects: ObjectFactory) {
        val extra = objects.mapProperty<String, Any>()

        operator fun <K : Any, V> MapProperty<K, V>.set(key: K, value: V?) {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            if (value != null) put(key, value)
        }
    }

    abstract class Plugin @Inject constructor(objects: ObjectFactory) : Config(objects) {
        val entrance: Property<String> = objects.property(String::class.java)
        val name: Property<String> = objects.property(String::class.java)
        val version: Property<String> = objects.property(String::class.java)

        val authors: ListProperty<String> = objects.listProperty(String::class.java).convention(emptyList())
        fun authors(vararg names: String) = authors.addAll(names.toList())

        val apiVersion: Property<String> = objects.property(String::class.java)
        /** Alias for [apiVersion]. */
        val api = apiVersion

        val description: Property<String> = objects.property(String::class.java)

        val dependencies: ListProperty<PluginDescriptor.Dependency> =
            objects.listProperty(PluginDescriptor.Dependency::class.java).convention(emptyList())
        fun dependencies(vararg plugins: PluginDescriptor.Dependency) = dependencies.addAll(plugins.toList())
        fun dependency(name: String, version: String? = null, optional: Boolean = false) =
            PluginDescriptor.Dependency(name, version, optional)

        val website: Property<String> = objects.property(String::class.java)

        operator fun <T> ListProperty<T>.plusAssign(item: T) = addAll(item)
    }

    abstract class Extension @Inject constructor(objects: ObjectFactory) : Config(objects)
}
