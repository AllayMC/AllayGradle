package org.allaymc.gradle.plugin.tasks

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.allaymc.gradle.plugin.data.PluginDependency
import org.allaymc.gradle.plugin.data.PluginDescriptor
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@OptIn(ExperimentalSerializationApi::class)
private val Serialization = Json {
    explicitNulls = false
    prettyPrint = true
}

abstract class GeneratePluginDescriptorTask : DefaultTask() {
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val pluginEntrance: Property<String>

    @get:Input
    @get:Optional
    abstract val pluginName: Property<String>

    @get:Input
    @get:Optional
    abstract val pluginVersion: Property<String>

    @get:Input
    @get:Optional
    abstract val pluginAuthors: ListProperty<String>

    @get:Input
    @get:Optional
    abstract val pluginApiVersion: Property<String>

    @get:Input
    @get:Optional
    abstract val pluginDescription: Property<String>

    @get:Input
    @get:Optional
    abstract val pluginDependencies: ListProperty<PluginDependency>

    @get:Input
    @get:Optional
    abstract val pluginWebsite: Property<String>

    @get:Input
    abstract val projectName: Property<String>

    @get:Input
    abstract val projectVersion: Property<String>

    @get:Input
    @get:Optional
    abstract val projectDescription: Property<String>

    @TaskAction
    @Suppress("UNCHECKED_CAST")
    fun generate() {
        val serializer = PluginDescriptor.serializer()
        val json = Serialization.encodeToJsonElement(serializer, createDescriptor()).jsonObject
        val mapSerializer = MapSerializer(String.serializer(), JsonElement.serializer())
        outputFile.get().asFile.writeText(Serialization.encodeToString(mapSerializer, json))
    }

    private fun createDescriptor(): PluginDescriptor {
        return PluginDescriptor(
            pluginEntrance.get().takeIf { it.isNotEmpty() }
                ?.let { if (it.startsWith(".")) "${project.group}$it" else it }
                ?: error("Entrance is not defined!"),
            pluginName.orNull ?: projectName.get().takeUnless { it == "unspecified" } ?: error("Name is not defined!"),
            (pluginVersion.orNull ?: projectVersion.get().takeUnless { it == "unspecified" }
            ?: error("Version is not defined!"))
                .takeIf { it.matches(semVerRegex) } ?: error("Version is invalid! (Please check https://semver.org/)"),
            pluginAuthors.getOrElse(emptyList()),
            pluginApiVersion.orNull,
            pluginDescription.orNull ?: projectDescription.orNull,
            pluginDependencies.get().takeUnless { it.isEmpty() },
            pluginWebsite.orNull,
        )
    }

    private val semVerRegex =
        Regex("^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$")

    internal class GeneratePluginDescriptorException(message: String) : Exception(message)

    internal fun error(message: String): Nothing = throw GeneratePluginDescriptorException(message)
}