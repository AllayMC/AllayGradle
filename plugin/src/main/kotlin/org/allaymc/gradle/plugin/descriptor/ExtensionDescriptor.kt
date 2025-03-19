package org.allaymc.gradle.plugin.descriptor

import kotlinx.serialization.Serializable

@Serializable
data class ExtensionDescriptor(
    val entrance: String?,
)
