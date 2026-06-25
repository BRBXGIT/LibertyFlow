plugins {
    // Compose Android Library
    alias(libs.plugins.libertyflow.compose.android.library)
}

dependencies {

    // Modules
    implementation(projects.core.domain)

    // BRBX
    implementation(libs.brbx.mvi.compose)
}