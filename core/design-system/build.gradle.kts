plugins {
    // Compose Android Library
    alias(libs.plugins.libertyflow.compose.android.library)
}

dependencies {

    // BRBX compose
    implementation(libs.brbx.ui.compose)
    // BRBX coil helpers
    implementation(libs.brbx.coil.helpers)
    // Solar
    implementation(libs.solar)
    // Paging
    implementation(libs.androidx.paging.compose)
    // Lottie
    implementation(libs.lottie)
    // Compose preview
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}