plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.allay.gradle)
}

allay {
    server = "0.8.3"  // or `null` for latest
    extension {
        entrance = "org.allaymc.gradle.sample.TestExtension"
    }
}
