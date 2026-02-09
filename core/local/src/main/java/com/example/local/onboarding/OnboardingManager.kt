package com.example.local.onboarding

import kotlinx.coroutines.flow.Flow

interface OnboardingManager {

    val isOnboardingCompleted: Flow<Boolean?>

    suspend fun saveOnboardingCompleted()
}