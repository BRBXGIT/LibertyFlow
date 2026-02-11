package com.example.onboarding.screen

sealed interface OnboardingIntent {
    data object SaveOnboardingCompleted: OnboardingIntent

    data object UpdateTriedToAskPermission: OnboardingIntent
}