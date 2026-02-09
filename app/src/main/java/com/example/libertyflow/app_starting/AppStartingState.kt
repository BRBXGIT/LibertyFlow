package com.example.libertyflow.app_starting

import androidx.compose.runtime.Immutable
import com.example.data.models.onboarding.OnboardingState

@Immutable
data class AppStartingState(
    val onboardingCompleted: OnboardingState = OnboardingState.Loading
)
