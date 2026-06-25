plugins {
    // Android Library
    alias(libs.plugins.libertyflow.android.library)
}

dependencies {

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    // Koin
    implementation(libs.koin.core)
}