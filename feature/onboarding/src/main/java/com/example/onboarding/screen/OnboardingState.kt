package com.example.onboarding.screen

import androidx.compose.runtime.Immutable

@Immutable
data class OnboardingState(
    val triedToAskPermission: Boolean = false
)