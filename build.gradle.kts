// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Application
    alias(libs.plugins.android.application) apply false
    // Kotlin
    alias(libs.plugins.kotlin.android) apply false
    // Compose compiler
    alias(libs.plugins.compose.compiler) apply false
    // Android
    alias(libs.plugins.android.library) apply false
    // Kotlin jvm
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    // Hilt
    alias(libs.plugins.hilt.android) apply false
    // Ksp
    alias(libs.plugins.ksp) apply false
    // Serialization
    alias(libs.plugins.kotlin.serialization) apply false
}