package org.allaymc.gradle.plugin.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PluginDescriptor(
    val entrance: String,
    val name: String,
    val version: String,
    val authors: List<String>,
    @SerialName("api_version")
    val apiVersion: String?,
    val description: String?,
    val dependencies: List<PluginDependency>?,
    val website: String?,
) : java.io.Serializable