plugins {
    // Android
    alias(libs.plugins.android.library)
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Compose compiler
    alias(libs.plugins.compose.compiler)
    // Ksp
    alias(libs.plugins.ksp)
    // Hilt
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.data"
    compileSdk = 36

    defaultConfig { minSdk = 28 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions { jvmTarget = "11" }

    buildFeatures { compose = true }
}

dependencies {

    // Core modules
    implementation(project(":core:network"))
    implementation(project(":core:local"))

    // Retrofit for hilt injection
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // Paging
    implementation(libs.androidx.paging.runtime.ktx)
    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Compose runtime
    implementation(libs.androidx.compose.runtime)
    // Room for hilt injection
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // === Testing ===
    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
}