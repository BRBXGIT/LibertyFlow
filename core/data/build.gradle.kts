plugins {
    // Android
    alias(libs.plugins.android.library)
    // Kotlin
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.data"
    compileSdk = 36

    defaultConfig { minSdk = 28 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
}

dependencies {

    // Core modules
    implementation(project(":core:network"))

    // Retrofit for `Response<T>`
    implementation(libs.retrofit)
}