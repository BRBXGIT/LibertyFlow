plugins {
    // Android Library
    alias(libs.plugins.libertyflow.android.library)
}

dependencies {

    // Modules
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.preferences)

    // Koin
    implementation(libs.koin.core)
}