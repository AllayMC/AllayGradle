package org.allaymc.gradle.plugin.descriptor

internal class DescriptorException(message: String) : Exception(message)

internal fun error(message: String): Nothing = throw DescriptorException(message)
