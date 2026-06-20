plugins {
    // Kotlin Library
    alias(libs.plugins.libertyflow.kotlin.library)
}

dependencies {

    // Paging
    implementation(libs.androidx.paging.common)
    // Koin
    implementation(libs.koin.core)
}