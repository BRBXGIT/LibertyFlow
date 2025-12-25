package com.example.home.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.navigation.HomeRoute
import com.example.home.screen.Home
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeVM

fun NavGraphBuilder.home(
    homeVM: HomeVM,
    navController: NavController
) = composable<HomeRoute> {
    val homeState by homeVM.homeState.collectAsStateWithLifecycle()
    val anime = homeVM.anime.collectAsLazyPagingItems()

    Home(
        anime = anime,
        homeState = homeState,
        onIntent = { intent ->
            when(intent) {
                is HomeIntent.NavigateToAnimeDetails -> { navController.navigate(AnimeDetailsRoute(intent.id)) }
                else -> homeVM.sendIntent(intent)
            }
        },
    )
}