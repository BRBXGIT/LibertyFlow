plugins {
    // Compose Android Library
    alias(libs.plugins.libertyflow.compose.android.library)
}

dependencies {

    // Core
    implementation(projects.core.domain)
    implementation(projects.core.designSystem)

    // BRBX
    implementation(libs.brbx.mvi.compose)
    // Paging
    implementation(libs.androidx.paging.compose)
    // Koin
    implementation(libs.koin.core)
}