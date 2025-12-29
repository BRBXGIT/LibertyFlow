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
import com.example.common.ui_helpers.HandleCommonEffects
import com.example.favorites.screen.Favorites
import com.example.favorites.screen.FavoritesVM

fun NavGraphBuilder.favorites(
    favoritesVM: FavoritesVM,
    navController: NavController
) = composable<FavoritesRoute> {
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