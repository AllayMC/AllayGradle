package org.allaymc.gradle.descriptor

import kotlinx.serialization.Serializable

@Serializable
data class PluginDescriptor(
    val entrance: String?,
    val name: String?,
    val version: String?,
    val authors: List<String>?,
    val description: String?,
    val dependencies: List<Dependency>?,
    val website: String?,
) {
    @Serializable
    data class Dependency(val name: String, val version: String?, val optional: Boolean?)
}
