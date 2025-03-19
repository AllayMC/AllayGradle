package org.allaymc.gradle

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import java.io.File

abstract class RunServerTask : JavaExec() {
    @get:InputFile
    abstract val shadowJar: RegularFileProperty

    @get:Input
    abstract val putExtension: Property<Boolean>

    @get:Input
    abstract val serverVersion: Property<String>

    @get:OutputDirectory
    val cwd: Provider<Directory> = project.layout.buildDirectory.dir("run")

    init {
        outputs.upToDateWhen { false }
        mainClass.set(Constant.SERVER_MAIN_CLASS)
    }

    @TaskAction
    override fun exec() {
        val jar = shadowJar.get().asFile
        val cwd = cwd.get().asFile
        val dir = cwd.resolve(if (putExtension.get()) "extensions" else "plugins").apply { mkdirs() }
        jar.copyTo(File(dir, jar.name), overwrite = true)

        val notation = "${Constant.DEPENDENCY_GROUP}:server:${serverVersion.get()}"
        val server = project.dependencies.create(notation)
        classpath = project.files(project.configurations.detachedConfiguration(server).resolve())
        workingDir = cwd

        super.exec()
    }
}
