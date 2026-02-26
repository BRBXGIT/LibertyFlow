plugins {
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Android
    alias(libs.plugins.android.library)
    // Compose compiler
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.android"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

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

    // Android tests
    api(libs.androidx.espresso.core)
    api(libs.androidx.ui.test.junit4)
    api(libs.androidx.ui.test.manifest)
    api(libs.androidx.test.ext.junit)
    api(libs.mockk.android)
}