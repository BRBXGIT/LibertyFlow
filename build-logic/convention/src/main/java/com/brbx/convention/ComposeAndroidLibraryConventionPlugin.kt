package com.brbx.convention

import com.brbx.convention.config.configureAndroidLibrary
import com.brbx.convention.config.configureCompose
import com.brbx.convention.config.configureOptics
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeAndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(receiver = target) {
            configureAndroidLibrary {
                configureCompose(commonExtension = this)
            }
            configureOptics()
        }
    }
}