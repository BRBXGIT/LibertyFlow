plugins {
    // Navigateable Compose Feature
    alias(libs.plugins.libertyflow.navigateable.feature)
}

dependencies {

    // Core
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.designSystem)
    // Feature
    implementation(projects.feature.common)

    // BRBX
    implementation(libs.brbx.mvi.compose)
    // Solar
    implementation(libs.solar)
    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    // Paging
    implementation(libs.androidx.paging.compose)
    // Compose preview
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}