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
import com.example.design_system.utils.standardScreenEnterTransition
import com.example.design_system.utils.standardScreenExitTransition
import com.example.onboarding.screen.Onboarding
import com.example.onboarding.screen.OnboardingVM

fun NavGraphBuilder.onboarding(navController: NavController) = composable<OnboardingRoute>(
    enterTransition = { standardScreenEnterTransition() },
    exitTransition = { standardScreenExitTransition() }
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