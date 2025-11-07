plugins {
    // Application
    alias(libs.plugins.android.application)
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Compose
    alias(libs.plugins.kotlin.compose)
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
}

dependencies {

    // Core modules
    implementation(project(":core:design-system"))

    // Core
    implementation(libs.androidx.core.ktx)
    // Activity
    implementation(libs.androidx.activity.compose)
    // Compose bom
    implementation(platform(libs.androidx.compose.bom))
    // Material 3
    implementation(libs.androidx.material3.android)
}