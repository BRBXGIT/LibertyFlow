plugins {
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Compose compiler
    alias(libs.plugins.compose.compiler)
    // Android
    alias(libs.plugins.android.library)
    // Ksp
    alias(libs.plugins.ksp)
    // Hilt
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.anime_details"
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

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}

dependencies {

    // Core modules
    implementation(project(":core:data"))
    implementation(project(":core:design-system"))
    implementation(project(":core:common"))

    // Compose animation graphics
    implementation(libs.androidx.animation.graphics)
    // Haze materials
    implementation(libs.haze.materials)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // Navigation
    implementation(libs.navigation.compose)
    // Material 3
    implementation(libs.androidx.material3.android)
    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Compose runtime
    implementation(libs.androidx.compose.runtime)
    // Compose debug
    debugImplementation(libs.androidx.ui.tooling)
}