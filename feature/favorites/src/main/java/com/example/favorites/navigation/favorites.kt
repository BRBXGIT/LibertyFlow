package com.example.favorites.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.navigation.FavoritesRoute
import com.example.favorites.screen.Favorites
import com.example.favorites.screen.FavoritesIntent
import com.example.favorites.screen.FavoritesVM

fun NavGraphBuilder.favorites(
    favoritesVM: FavoritesVM,
    navController: NavController
) = composable<FavoritesRoute> {
    val favoritesState by favoritesVM.favoritesState.collectAsStateWithLifecycle()
    val favorites = favoritesVM.favorites.collectAsLazyPagingItems()

    Favorites(
        favoritesState = favoritesState,
        favorites = favorites,
        onIntent = { intent ->
            when(intent) {
                is FavoritesIntent.NavigateToAnimeDetails -> { navController.navigate(AnimeDetailsRoute(intent.id)) }
                else -> favoritesVM.sendIntent(intent)
            }
        },
    )
}