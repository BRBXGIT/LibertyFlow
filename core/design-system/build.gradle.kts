plugins {
    // Android
    alias(libs.plugins.android.library)
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Compose compiler
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.design_system"
    compileSdk = 36

    defaultConfig { minSdk = 28 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures { compose = true }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}

dependencies {

    // Core modules
    implementation(project(":core:data"))
    implementation(project(":core:common"))

    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Compose runtime
    implementation(libs.androidx.compose.runtime)
    // Compose preview
    implementation(libs.androidx.ui.tooling.preview)
    // Material 3
    implementation(libs.androidx.material3.android)
    // Compose animation graphics
    implementation(libs.androidx.animation.graphics)
    // Coil
    implementation(libs.coil.compose)
    // Paging
    implementation(libs.androidx.paging.compose)
    // Lottie
    implementation(libs.lottie)
}