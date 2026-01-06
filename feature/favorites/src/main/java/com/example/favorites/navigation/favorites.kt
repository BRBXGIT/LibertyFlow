package com.example.favorites.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.FavoritesRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.standardScreenEnterTransition
import com.example.design_system.utils.standardScreenExitTransition
import com.example.favorites.screen.Favorites
import com.example.favorites.screen.FavoritesVM

fun NavGraphBuilder.favorites(
    favoritesVM: FavoritesVM,
    navController: NavController
) = composable<FavoritesRoute>(
    enterTransition = { standardScreenEnterTransition() },
    exitTransition = { standardScreenExitTransition() }
) {
    val state by favoritesVM.state.collectAsStateWithLifecycle()
    val favorites = favoritesVM.favorites.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle navigation & snackbar side-effects
    HandleCommonEffects(
        effects = favoritesVM.effects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    Favorites(
        state = state,
        favorites = favorites,
        snackbarHostState = snackbarHostState,
        onIntent = favoritesVM::sendIntent,
        onEffect = favoritesVM::sendEffect
    )
}