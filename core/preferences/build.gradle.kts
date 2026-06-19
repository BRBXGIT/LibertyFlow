plugins {
    // Android Library
    alias(libs.plugins.libertyflow.android.library)
}

dependencies {

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    // Datastore
    implementation(libs.androidx.datastore.preferences)
}