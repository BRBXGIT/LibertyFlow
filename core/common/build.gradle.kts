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
    // Compose compiler
    alias(libs.plugins.compose.compiler)
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

    // Paging
    implementation(libs.androidx.paging.compose)
    // Nav
    implementation(libs.navigation.compose)
    // Material 3
    implementation(libs.androidx.material3.android)
    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Compose runtime
    implementation(libs.androidx.compose.runtime)
    // Lifecycle ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}