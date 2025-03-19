package org.allaymc.gradle.descriptor

import kotlinx.serialization.Serializable

@Serializable
data class ExtensionDescriptor(
    val entrance: String?,
)
