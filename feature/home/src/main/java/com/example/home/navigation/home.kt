package com.example.home.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.HomeRoute
import com.example.home.screen.Home
import com.example.home.screen.HomeVM

fun NavGraphBuilder.home(
    homeVM: HomeVM
) = composable<HomeRoute> {
    val homeState by homeVM.homeState.collectAsStateWithLifecycle()
    val animeByQuery = homeVM.animeByQuery.collectAsLazyPagingItems()
    val homeEffect = homeVM.homeEffect

    Home(
        onIntent = homeVM::sendIntent,
        animeByQuery = animeByQuery,
        homeState = homeState,
        homeEffect = homeEffect
    )
}