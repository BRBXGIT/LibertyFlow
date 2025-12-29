package com.example.favorites.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.FavoritesRoute
import com.example.common.ui_helpers.HandleCommonEffects
import com.example.design_system.constants.ScreenTransitionConstants.ANIMATION_DURATION
import com.example.favorites.screen.Favorites
import com.example.favorites.screen.FavoritesVM

fun NavGraphBuilder.favorites(
    favoritesVM: FavoritesVM,
    navController: NavController
) = composable<FavoritesRoute>(
    enterTransition = { fadeIn(tween(ANIMATION_DURATION)) },
    exitTransition = { fadeOut(tween(ANIMATION_DURATION)) }
) {
    val favoritesState by favoritesVM.favoritesState.collectAsStateWithLifecycle()
    val favoritesEffects = favoritesVM.favoritesEffects

    val favorites = favoritesVM.favorites.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommonEffects(
        effects = favoritesEffects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    Favorites(
        favoritesState = favoritesState,
        favorites = favorites,
        snackbarHostState = snackbarHostState,
        onIntent = favoritesVM::sendIntent,
        onEffect = favoritesVM::sendEffect
    )
}