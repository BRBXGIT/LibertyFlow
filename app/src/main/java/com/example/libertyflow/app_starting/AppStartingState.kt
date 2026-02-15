package com.example.libertyflow.app_starting

import androidx.compose.runtime.Immutable
import com.example.data.models.onboarding.OnboardingState

/**
 * Represents the initial configuration state of the application upon launch.
 *
 * @property onboardingCompleted The current status of the user's onboarding progress.
 * Defaults to [OnboardingState.Loading] to prevent UI flickering or premature
 * navigation before the repository responds.
 */
@Immutable
data class AppStartingState(
    val onboardingCompleted: OnboardingState = OnboardingState.Loading
)
