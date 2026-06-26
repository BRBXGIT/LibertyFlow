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
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}