package com.example.data.models.onboarding

sealed interface OnboardingState {
    data object Loading: OnboardingState
    data object Completed: OnboardingState
    data object NotCompleted: OnboardingState
}