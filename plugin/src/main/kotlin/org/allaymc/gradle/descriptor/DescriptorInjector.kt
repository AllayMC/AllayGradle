package org.allaymc.gradle.descriptor

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import org.allaymc.gradle.AllayExtension
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

private val Serialization = Json {
    explicitNulls = false
}

@OptIn(InternalSerializationApi::class)
fun descriptorInject(project: Project, extension: AllayExtension) {
    val isExtension = extension.isExtension.get()
    project.tasks.named("processResources") {
        @Suppress("UNCHECKED_CAST")
        doLast {
            val fileName = "${if (isExtension) "extension" else "plugin"}.json"
            val fileOrigin = project.file("src/main/resources/$fileName")
            val fileProcessed = project.file("${project.layout.buildDirectory.get()}/resources/main/$fileName")

            val content = runCatching { fileOrigin.readText() }.getOrNull() ?: "{}"
            val serializer = (if (isExtension) ExtensionDescriptor::class else PluginDescriptor::class).serializer()
            val parsed = Serialization.decodeFromString(serializer, content)

            val processed = (parsed as? ExtensionDescriptor)?.inject(project, extension.extension)
                ?: (parsed as PluginDescriptor).inject(project, extension.plugin)

            val baseJson = Serialization.encodeToJsonElement(serializer as KSerializer<Any>, processed).jsonObject
            val extraJson = buildJsonObject {
                val extra = if (isExtension) extension.extension.extra else extension.plugin.extra
                extra.get().forEach { (k, v) -> put(k, Serialization.encodeToJsonElement(v::class.serializer() as KSerializer<Any>, v)) }
            }

            fileProcessed.writeText(Serialization.encodeToString(baseJson + extraJson))
        }
    }
}

private fun PluginDescriptor.inject(project: Project, extension: AllayExtension.Plugin) = copy(
    entrance = (entrance ?: extension.entrance.orNull).ensureEntrance(project),
    name = name.template(extension.name),
    version = version.ensureVersion(project, extension.version),
    authors = authors + extension.authors,
    description = description.template(extension.description),
    dependencies = dependencies + extension.dependencies,
    website = website.template(extension.website),
)

private fun ExtensionDescriptor.inject(project: Project, extension: AllayExtension.Extension) = copy(
    entrance = (entrance ?: extension.entrance.orNull).ensureEntrance(project),
)

private fun String?.ensureEntrance(project: Project) = this
    ?.takeIf { it.isNotEmpty() }
    ?.let { if (it.startsWith(".")) "${project.group}$it" else it }
    ?: error("Entrance is not defined!")

private val SemVerRegex =
    Regex("^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$")
private fun String?.ensureVersion(project: Project, property: Property<String>) =
    ((this ?: project.version.toString().takeUnless { it == "unspecified" }).template(property)
        ?: error("Version is not defined!")).takeIf { it.matches(SemVerRegex) }
        ?: error("Version doesn't valid! (Use https://semver.org/)")

private fun String?.template(property: Property<String>) =
    this?.replace("\${it}", property.orNull ?: "") ?: property.orNull

private operator fun <T> List<T>?.plus(property: ListProperty<T>) =
    (this ?: emptyList()) + property.get()
