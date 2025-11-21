package com.example.favorites.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.FavoritesRoute
import com.example.favorites.screen.Favorites
import com.example.favorites.screen.FavoritesVM

fun NavGraphBuilder.favorites(
    favoritesVM: FavoritesVM
) = composable<FavoritesRoute> {
    val favoritesState by favoritesVM.favoritesState.collectAsStateWithLifecycle()
    val favorites = favoritesVM.favorites.collectAsLazyPagingItems()

    Favorites(
        favoritesState = favoritesState,
        onIntent = favoritesVM::sendIntent,
        favorites = favorites,
    )
}