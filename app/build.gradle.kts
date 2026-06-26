plugins {
    // Android Application
    alias(libs.plugins.libertyflow.android.application)
}

dependencies {

    // Core
    implementation(projects.core.network)
    implementation(projects.core.preferences)
    implementation(projects.core.localDbs)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    // Feature
    implementation(projects.feature.common)
    implementation(projects.feature.home)

    // Core
    implementation(libs.androidx.core.ktx)
    // BRBX
    implementation(libs.brbx.mvi.compose)
    // Koin
    implementation(libs.koin.android)
}