package com.example.settings.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.SettingsRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.fadeScreenEnterTransition
import com.example.design_system.utils.fadeScreenExitTransition
import com.example.settings.screen.Settings
import com.example.settings.screen.SettingsVM

/**
 * Extension function for [NavGraphBuilder] to register the Settings screen destination.
 * * This function encapsulates the boilerplate required to initialize the Settings
 * feature within the navigation graph, including:
 * 1. **Custom Transitions**: Applies fade-in and fade-out animations for screen entry/exit.
 * 2. **State Management**: Connects a [hiltViewModel] to the [Settings] UI.
 * 3. **Lifecycle-Awareness**: Uses [collectAsStateWithLifecycle] to safely observe UI state.
 * 4. **Side Effects**: Integrates [HandleCommonEffects] to process one-time events
 * (like navigation or snackbars) dispatched from the ViewModel.
 *
 * @param navController The [NavController] used for navigating away from the Settings screen.
 */
fun NavGraphBuilder.settings(navController: NavController) = composable<SettingsRoute>(
    enterTransition = { fadeScreenEnterTransition() },
    exitTransition = { fadeScreenExitTransition() }
) {
    val settingsVM = hiltViewModel<SettingsVM>()

    val state by settingsVM.state.collectAsStateWithLifecycle()

    HandleCommonEffects(
        effects = settingsVM.effects,
        navController = navController
    )

    Settings(
        state = state,
        onIntent = settingsVM::sendIntent,
        onEffect = settingsVM::sendEffect
    )
}