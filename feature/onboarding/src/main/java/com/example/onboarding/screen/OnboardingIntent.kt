package com.example.onboarding.screen

/**
 * Represents the available user or system actions within the Onboarding flow.
 *
 * These intents are dispatched from the UI layer to the [OnboardingVM]
 * to trigger state updates or business logic execution.
 */
sealed interface OnboardingIntent {
    data object SaveOnboardingCompleted: OnboardingIntent

    data object UpdateTriedToAskPermission: OnboardingIntent
}