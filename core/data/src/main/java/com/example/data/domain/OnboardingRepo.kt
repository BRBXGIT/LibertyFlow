package com.example.data.domain

import com.example.data.models.onboarding.OnboardingState
import kotlinx.coroutines.flow.Flow

interface OnboardingRepo {

    val onboardingState: Flow<OnboardingState>

    suspend fun saveOnboardingCompleted()
}