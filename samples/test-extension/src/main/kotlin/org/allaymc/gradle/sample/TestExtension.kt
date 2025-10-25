package org.allaymc.gradle.sample

import org.allaymc.server.extension.Extension

@Suppress("unused")
class TestExtension : Extension() {
    override fun main(args: Array<String>?) =
        println("Hello from TestExtension!")
}
