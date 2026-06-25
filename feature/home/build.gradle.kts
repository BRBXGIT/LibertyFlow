plugins {
    // Navigateable Compose Feature
    alias(libs.plugins.libertyflow.navigateable.feature)
}

dependencies {

    // Core
    implementation(projects.core.domain)
    implementation(projects.core.common)
    // Feature
    implementation(projects.feature.common)

    // BRBX
    implementation(libs.brbx.mvi.compose)
    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    // Paging
    implementation(libs.androidx.paging.compose)
}