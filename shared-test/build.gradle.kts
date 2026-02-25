plugins {
    // Android
    alias(libs.plugins.android.library)
    // Kotlin
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.shared_test"
    compileSdk = 36

    defaultConfig { minSdk = 28 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
}

dependencies {

    // --- Testing ---
    // Unit tests
    api(libs.mockk)
    api(libs.junit)
    api(libs.coroutines.test)
    api(libs.turbine)
    // Android tests
    api(libs.androidx.espresso.core)
    api(libs.androidx.ui.test.junit4)
    api(libs.androidx.ui.test.manifest)
    api(libs.androidx.test.ext.junit)
    api(libs.mockk.android)
}