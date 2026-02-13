package com.example.local.onboarding

import kotlinx.coroutines.flow.Flow

interface OnboardingPrefsManager {

    val isOnboardingCompleted: Flow<Boolean?>

    suspend fun saveOnboardingCompleted()
}