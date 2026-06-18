plugins {
    // Kotlin Library
    alias(libs.plugins.libertyflow.kotlin.library)
    // Serialixation
    alias(libs.plugins.kotlin.serialization)
}

dependencies {

    // Koin
    implementation(libs.koin.core)
    // Ktor
    implementation(libs.bundles.ktor.core)
}