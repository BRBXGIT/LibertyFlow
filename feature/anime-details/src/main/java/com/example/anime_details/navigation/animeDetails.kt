package com.example.anime_details.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.anime_details.screen.AnimeDetails
import com.example.anime_details.screen.AnimeDetailsVM
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.refresh.RefreshVM
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.fadeScreenEnterTransition
import com.example.design_system.utils.fadeScreenExitTransition
import com.example.player.player.PlayerVM

/**
 * Defines the navigation destination for the Anime Details screen within the [NavGraphBuilder].
 *
 * This function sets up the [AnimeDetailsVM] using Hilt and connects it to the
 * [AnimeDetails] UI. It also orchestrates communication between multiple ViewModels:
 * - [RefreshVM]: For invalidating global data caches (e.g., favorites screen paging).
 * - [PlayerVM]: For initiating video playback and passing episode data.
 *
 * @param refreshVM Shared ViewModel to handle cross-screen data refreshing.
 * @param playerVM Shared ViewModel to handle video player logic and intent routing.
 * @param navController Controller to handle back-navigation or deep-linking.
 */
fun NavGraphBuilder.animeDetails(
    refreshVM: RefreshVM,
    playerVM: PlayerVM,
    navController: NavController
) = composable<AnimeDetailsRoute>(
    enterTransition = { fadeScreenEnterTransition() },
    exitTransition = { fadeScreenExitTransition() }
) {
    val animeDetailsVM = hiltViewModel<AnimeDetailsVM>()

    val animeDetailsState by animeDetailsVM.state.collectAsStateWithLifecycle()
    val animeDetailsEffects = animeDetailsVM.effects

    val snackbarHostState = remember { SnackbarHostState() }

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
        onRefreshEffect = refreshVM::sendEffect,
        onPlayerIntent = playerVM::sendIntent
    )
}