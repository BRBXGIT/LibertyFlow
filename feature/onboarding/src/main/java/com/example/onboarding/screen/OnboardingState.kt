package com.example.onboarding.screen

import androidx.compose.runtime.Immutable

/**
 * Represents the UI state for the Onboarding screen.
 *
 * This class is marked with [@Immutable] to inform the Compose compiler that
 * all public properties will not change after the object is constructed,
 * enabling more efficient recomposition.
 *
 * @property triedToAskPermission Tracks whether the user has been prompted for
 * system permissions (e.g., Notifications) to avoid redundant prompts.
 */
@Immutable
data class OnboardingState(
    val triedToAskPermission: Boolean = false
)