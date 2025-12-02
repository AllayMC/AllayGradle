package org.allaymc.gradle.plugin.tasks

import org.allaymc.gradle.plugin.Constants
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import java.io.File

abstract class RunServerTask : JavaExec() {
    @get:InputFile
    @get:Optional
    abstract val pluginJar: RegularFileProperty

    @get:Input
    abstract val serverVersion: Property<String>

    @get:OutputDirectory
    val cwd: Provider<Directory> = project.layout.buildDirectory.dir("run")

    init {
        group = Constants.TASK_GROUP
        outputs.upToDateWhen { false }
        mainClass.set(Constants.SERVER_MAIN_CLASS)
    }

    @TaskAction
    override fun exec() {
        pluginJar.loadTo("plugins")

        val notation = "${Constants.DEPENDENCY_GROUP}:server:${serverVersion.get()}"
        val server = project.dependencies.create(notation)
        classpath = project.files(project.configurations.detachedConfiguration(server).resolve())
        workingDir = cwd.get().asFile
        super.exec()
    }

    private fun RegularFileProperty.loadTo(name: String) {
        val file = orNull?.asFile ?: return
        val dest = cwd.get().asFile.resolve(name).apply { mkdirs() }
        file.copyTo(File(dest, file.name), overwrite = true)
    }
}