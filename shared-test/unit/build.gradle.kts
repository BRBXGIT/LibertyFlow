plugins {
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Android
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.unit"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}

dependencies {

    // --- Testing ---
    // Unit
    api(libs.mockk)
    api(libs.junit)
    api(libs.coroutines.test)
    api(libs.turbine)
}