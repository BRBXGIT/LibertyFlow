package com.example.settings.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.SettingsRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.standardScreenEnterTransition
import com.example.design_system.utils.standardScreenExitTransition
import com.example.settings.screen.Settings
import com.example.settings.screen.SettingsVM

fun NavGraphBuilder.settings(navController: NavController) = composable<SettingsRoute>(
    enterTransition = { standardScreenEnterTransition() },
    exitTransition = { standardScreenExitTransition() }
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