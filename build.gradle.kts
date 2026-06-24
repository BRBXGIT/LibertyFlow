plugins {
    // Application
    alias(libs.plugins.android.application) apply false
    // Compose
    alias(libs.plugins.kotlin.compose) apply false
    // Kotlin jvm
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    // Ksp
    alias(libs.plugins.ksp) apply false
    // Android library
    alias(libs.plugins.android.library) apply false
    // Serialization
    alias(libs.plugins.kotlin.serialization) apply false
}