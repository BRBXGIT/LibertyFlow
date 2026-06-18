// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Application
    alias(libs.plugins.android.application) apply false
    // Compose
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}