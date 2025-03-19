package org.allaymc.gradle.descriptor

internal class DescriptorException(message: String) : Exception(message)

internal fun error(message: String): Nothing = throw DescriptorException(message)
