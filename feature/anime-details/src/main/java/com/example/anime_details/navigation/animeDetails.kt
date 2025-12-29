package com.example.anime_details.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.anime_details.screen.AnimeDetails
import com.example.anime_details.screen.AnimeDetailsVM
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.HandleCommonEffects
import com.example.design_system.constants.ScreenTransitionConstants.ANIMATION_DURATION

fun NavGraphBuilder.animeDetails(
    navController: NavController
) = composable<AnimeDetailsRoute>(
    enterTransition = {
        slideInHorizontally(
            initialOffsetX = { it / 2 },
            animationSpec = tween(ANIMATION_DURATION)
        ) + fadeIn(tween(ANIMATION_DURATION - 100))
    },
    exitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it / 2 },
            animationSpec = tween(ANIMATION_DURATION)
        ) + fadeOut(tween(ANIMATION_DURATION - 100))
    }
) {
    val route = it.toRoute<AnimeDetailsRoute>()

    val animeDetailsVM = hiltViewModel<AnimeDetailsVM>()

    val animeDetailsState by animeDetailsVM.animeDetailsState.collectAsStateWithLifecycle()
    val animeDetailsEffects = animeDetailsVM.animeDetailsEffects

    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommonEffects(
        effects = animeDetailsEffects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    AnimeDetails(
        animeId = route.animeId,
        animeDetailsState = animeDetailsState,
        snackbarHostState = snackbarHostState,
        onEffect = animeDetailsVM::sendEffect,
        onIntent = animeDetailsVM::sendIntent
    )
}