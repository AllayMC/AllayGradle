package org.allaymc.gradle.plugin.data

import kotlinx.serialization.Serializable

@Serializable
data class PluginDependency(val name: String, val version: String?, val optional: Boolean?) : java.io.Serializable