package com.brbx.convention

import com.brbx.convention.config.configureAndroidLibrary
import com.brbx.convention.config.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeAndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureAndroidLibrary {
                configureCompose(this)
            }
        }
    }
}