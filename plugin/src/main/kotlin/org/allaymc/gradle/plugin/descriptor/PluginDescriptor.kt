package org.allaymc.gradle.plugin.descriptor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PluginDescriptor(
    val entrance: String?,
    val name: String?,
    val version: String?,
    val authors: List<String>?,
    @SerialName("api_version")
    val api: String?,
    val description: String?,
    val dependencies: List<Dependency>?,
    val website: String?,
) {
    @Serializable
    data class Dependency(val name: String, val version: String?, val optional: Boolean?)
}
