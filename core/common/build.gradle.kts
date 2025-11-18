plugins {
    // Android
    alias(libs.plugins.android.library)
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Ksp
    alias(libs.plugins.ksp)
    // Hilt
    alias(libs.plugins.hilt.android)
    // Serialization
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.common"
    compileSdk = 36

    defaultConfig { minSdk = 28 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions { jvmTarget = "11" }

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}

dependencies {

    // Core modules
    implementation(project(":core:data"))

    // Lifecycle ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}