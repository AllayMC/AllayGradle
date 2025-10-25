# Allay Gradle Plugin

A Gradle plugin designed to boost Allay plugin development!

## Usage

```kt
allay {
    // API dependency version (required if `apiOnly = true`).
    api = "0.15.0"

    // Specify server version for `runServer` and dependency version when `apiOnly = false`.
    // Set to null for the latest version.
    server = null

    // Depend on the server module instead of the API module for server-specific features.
    apiOnly = true

    // Configure the plugin descriptor.
    plugin {
        entrance = "org.allaymc.gradle.sample.TestPlugin"
        // Or use relative path if project's group is set.
        // entrance = ".TestPlugin"

        name = "TestPlugin"
        version = "0.1.0-alpha"
        authors += "Cdm2883"
        api = ">= 0.15.0"
        description = "Test plugin for AllayGradle!"
        website = "https://github.com/AllayMC/AllayGradle"
    }
}
```

> See a full example in [sample/build.gradle.kts](sample/build.gradle.kts).
