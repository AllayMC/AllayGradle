# Allay Gradle Plugin

A Gradle plugin designed to boost Allay plugin development!

## Usage

```kt

group = "org.allaymc.gradle.sample"
version = "0.1.0"
description = "Test plugin for AllayGradle!"

allay {
    // API version (required if `apiOnly = true`).
    api = "0.15.0"

    // Set this field to `false` to access the server module to use the internal APIs. However, this is not
    // recommended as internal APIs can change at any time.
    // The default value is `true`.
    apiOnly = true

    // Specify the server version used in the `runServer` task. This will also be the dependency version if
    // `apiOnly` is set to `false`. If this field is set to `null`, the latest server version will be used.
    // The default value is `null`.
    server = null

    // Configure the plugin descriptor (plugin.json).
    plugin {
        entrance = "org.allaymc.gradle.sample.TestPlugin"
        // Or use the relative path if the project's group is set.
        // entrance = ".TestPlugin"

        apiVersion = ">=0.15.0"
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
