plugins {
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Compose
    alias(libs.plugins.kotlin.compose)
    // Android
    alias(libs.plugins.android.library)
    // Serialization
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.home"
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

    // Core modules
    implementation(project(":core:data"))
    implementation(project(":core:design-system"))
    implementation(project(":core:common"))

    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Nav
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    // Material 3
    implementation(libs.androidx.material3.android)
}