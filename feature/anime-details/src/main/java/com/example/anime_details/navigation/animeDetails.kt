package com.example.anime_details.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.anime_details.screen.AnimeDetails
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.anime_details.screen.AnimeDetailsVM
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.refresh.RefreshVM
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.standardScreenEnterTransition
import com.example.design_system.utils.standardScreenExitTransition

fun NavGraphBuilder.animeDetails(
    refreshVM: RefreshVM,
    navController: NavController
) = composable<AnimeDetailsRoute>(
    enterTransition = { standardScreenEnterTransition() },
    exitTransition = { standardScreenExitTransition() }
) {
    val route = it.toRoute<AnimeDetailsRoute>()

    val animeDetailsVM = hiltViewModel<AnimeDetailsVM>()

    val animeDetailsState by animeDetailsVM.state.collectAsStateWithLifecycle()
    val animeDetailsEffects = animeDetailsVM.effects

    val snackbarHostState = remember { SnackbarHostState() }

    HandleAnimeData(route.animeId, animeDetailsVM::sendIntent)

    // Handle effects
    HandleCommonEffects(
        effects = animeDetailsEffects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    AnimeDetails(
        state = animeDetailsState,
        snackbarHostState = snackbarHostState,
        onEffect = animeDetailsVM::sendEffect,
        onIntent = animeDetailsVM::sendIntent,
        onRefreshEffect = refreshVM::sendEffect
    )
}

@Composable
private fun HandleAnimeData(
    animeId: Int,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    LaunchedEffect(Unit) {
        onIntent(AnimeDetailsIntent.FetchAnime(animeId))
        onIntent(AnimeDetailsIntent.ObserveWatchedEps(animeId))
    }
}