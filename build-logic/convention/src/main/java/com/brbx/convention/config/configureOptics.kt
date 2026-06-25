package com.brbx.convention.config

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.configureOptics() {
    pluginManager.apply("com.google.devtools.ksp")

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        add(
            configurationName = "implementation",
            dependencyNotation = libs.findLibrary("arrow-optics").get()
        )
        add(
            configurationName = "ksp",
            dependencyNotation = libs.findLibrary("arrow-optics-ksp").get()
        )
    }
}