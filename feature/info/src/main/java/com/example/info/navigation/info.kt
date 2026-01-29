package com.example.info.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.InfoRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.info.screen.Info
import com.example.info.screen.InfoVM

fun NavGraphBuilder.info(navController: NavController) = composable<InfoRoute> {
    val infoVM = hiltViewModel<InfoVM>()

    HandleCommonEffects(
        effects = infoVM.effects,
        navController = navController
    )

    Info(onEffect = infoVM::sendEffect)
}