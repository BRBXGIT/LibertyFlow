plugins {
    // Application
    alias(libs.plugins.android.application)
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
    namespace = "com.example.libertyflow"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.libertyflow"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    implementation(project(":core:design-system"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    // Feature modules
    implementation(project(":feature:home"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:collections"))
    implementation(project(":feature:anime-details"))
    implementation(project(":feature:more"))

    // Core
    implementation(libs.androidx.core.ktx)
    // Activity
    implementation(libs.androidx.activity.compose)
    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Compose runtime
    implementation(libs.androidx.compose.runtime)
    // Material 3
    implementation(libs.androidx.material3.android)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // Nav
    implementation(libs.navigation.compose)
}