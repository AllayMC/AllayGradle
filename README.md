# Allay Gradle Plugin

[![Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/org.allaymc.gradle.plugin)](https://plugins.gradle.org/plugin/org.allaymc.gradle.plugin)
[![License](https://img.shields.io/github/license/allaymc/allaygradle)](LICENSE)

A Gradle plugin designed to boost Allay plugin development!

## Feature

- Automatically configure the maven dependencies and repositories needed to develop allay plugins.
- Automatically generate the plugin descriptor (plugin.json) based on the project's metadata.
- Use the `runServer` task to quickly build and debug your plugin.

## Quick Start

It is pretty simple to use this plugin. Just add the following code to your `build.gradle.kts` file:

```kt
plugins {
    id("java-library")
    id("org.allaymc.gradle.plugin").version("0.2.1")
}

group = "org.allaymc.gradle.sample"
version = "0.1.0"
description = "Test plugin for AllayGradle!"

allay {
    // API version (required if `apiOnly = true`).
    api = "0.16.0"

    // Set this field to `false` to access the server module to use the internal APIs. However, this is not
    // recommended as internal APIs can change at any time.
    // The default value is `true`.
    apiOnly = true

    // Specify the server version used in the `runServer` task. This will also be the dependency version if
    // `apiOnly` is set to `false`. If this field is set to `null`, the latest server version will be used.
    // The default value is `null`.
    server = null

    // Whether to generate the plugin descriptor (plugin.json).
    // The default value is `true`.
    generatePluginDescriptor = true

    // Configure the plugin descriptor (plugin.json).
    // This would be ignored if `generatePluginDescriptor` is set to `false`.
    plugin {
        entrance = "org.allaymc.gradle.sample.TestPlugin"
        // Or use the relative path if the project's group is set.
        // entrance = ".TestPlugin"

        apiVersion = ">=0.16.0"
        // `api = ">=0.16.0" is equivalent here.
        
        authors += "Cdm2883"
        website = "https://github.com/AllayMC/AllayGradle"
        
        // By default, the following fields are set to the project's group, version, and description.
        // However, you can override them if you want.
        // name = "..."
        // version = "..."
        // description = "..."
    }
}
```

## Run Server Task

The plugin provides a `runServer` task to quickly build and debug your plugin, which will do the following things:

- Build your plugin into a shadow jar.
- Copy the generated shadow jar from the build output to the test server's plugins directory (`build/run/plugins`).
- Launch a test server in the `build/run/server` directory. Your plugin will be loaded along with the test server.

## Shadow Jar Task

The plugin provides a basic `shadowJar` task if neither `com.gradle.up` nor `com.github.johnrengelman.shadow` plugin is
applied, which handles the fundamental jar building and dependency shading functionality.

For more information about the Allay project, please refer to https://docs.allaymc.org/.

## 🎫 License

Copyright **© 2023-2025 AllayMC**, all rights reserved. Project content is open source under the LGPL-3.0 license.