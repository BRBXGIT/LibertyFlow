package com.example.home.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.HomeRoute
import com.example.common.ui_helpers.HandleCommonEffects
import com.example.home.screen.Home
import com.example.home.screen.HomeVM

fun NavGraphBuilder.home(
    homeVM: HomeVM,
    navController: NavController
) = composable<HomeRoute> {
    val homeState by homeVM.homeState.collectAsStateWithLifecycle()
    val effects = homeVM.effects

    val anime = homeVM.anime.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommonEffects(
        effects = effects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    Home(
        anime = anime,
        homeState = homeState,
        snackbarHostState = snackbarHostState,
        onIntent = homeVM::sendIntent,
        onEffect = homeVM::sendEffect
    )
}