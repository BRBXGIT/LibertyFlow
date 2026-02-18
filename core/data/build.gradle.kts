plugins {
    // Android
    alias(libs.plugins.android.library)
    // Kotlin
    alias(libs.plugins.kotlin.android)
    // Compose compiler(for @Immutable annotation)
    alias(libs.plugins.compose.compiler)
    // Ksp
    alias(libs.plugins.ksp)
    // Hilt
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.data"
    compileSdk = 36

    defaultConfig { minSdk = 28 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures { compose = true } // For @Immutable annotation

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

dependencies {

    // --- Modules ---
    // Core
    implementation(project(":core:network"))
    implementation(project(":core:local"))

    // --- Libraries ---
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // Paging
    implementation(libs.androidx.paging.runtime.ktx)
    // Compose bom(for @Immutable annotation)
    implementation(platform(libs.androidx.compose.bom))
    // Compose runtime(for @Immutable annotation)
    implementation(libs.androidx.compose.runtime)
    // Media 3 for hilt injection
    implementation(libs.androidx.media3.exoplayer)
    // Media 3 for player session
    api(libs.androidx.media3.session) // Api cause dagger need 'Service' class for playback

    // --- Testing ---
    testImplementation(project(":shared-test:core-testing"))
}