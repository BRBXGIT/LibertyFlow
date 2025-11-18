plugins {
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Compose compiler
    alias(libs.plugins.compose.compiler)
    // Android
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.player"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
    buildFeatures { compose = true }
}

dependencies {

    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Compose runtime
    implementation(libs.androidx.compose.runtime)
}