plugins {
    // Android Application
    alias(libs.plugins.libertyflow.android.application)
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    // BRBX
    implementation(libs.brbx.mvi.compose)
}