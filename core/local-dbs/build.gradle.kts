plugins {
    // Android Library
    alias(libs.plugins.libertyflow.android.library)
    // Ksp
    alias(libs.plugins.ksp)
}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}

dependencies {

    // Room
    implementation(libs.bundles.room.core)
    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}