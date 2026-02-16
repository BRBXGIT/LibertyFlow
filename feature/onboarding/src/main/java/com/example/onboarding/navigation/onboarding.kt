package com.example.onboarding.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.OnboardingRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.fadeScreenEnterTransition
import com.example.design_system.utils.fadeScreenExitTransition
import com.example.onboarding.screen.Onboarding
import com.example.onboarding.screen.OnboardingVM

/**
 * Defines the navigation entry point for the Onboarding screen.
 * * This extension:
 * 1. Scopes the [OnboardingVM] to the navigation backstack entry.
 * 2. Collects UI state in a lifecycle-aware manner using [collectAsStateWithLifecycle].
 * 3. Bridges the ViewModel's effect stream to the UI via [HandleCommonEffects].
 *
 * @param navController The controller used for potential navigation within [HandleCommonEffects].
 */
fun NavGraphBuilder.onboarding(navController: NavController) = composable<OnboardingRoute>(
    enterTransition = { fadeScreenEnterTransition() },
    exitTransition = { fadeScreenExitTransition() }
) {
    val onboardingVM = hiltViewModel<OnboardingVM>()

    val state by onboardingVM.state.collectAsStateWithLifecycle()

    val snackbarHostState = SnackbarHostState()

    HandleCommonEffects(
        effects = onboardingVM.effects,
        snackbarHostState = snackbarHostState,
        navController = navController
    )

    Onboarding(
        state = state,
        onIntent = onboardingVM::sendIntent,
        onEffect = onboardingVM::sendEffect
    )
}