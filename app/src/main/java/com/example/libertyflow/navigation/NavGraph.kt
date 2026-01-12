@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.libertyflow.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.anime_details.navigation.animeDetails
import com.example.collections.navigation.collections
import com.example.collections.screen.CollectionsVM
import com.example.common.navigation.CollectionsRoute
import com.example.common.navigation.FavoritesRoute
import com.example.common.navigation.HomeRoute
import com.example.common.navigation.MoreRoute
import com.example.common.navigation.NavBarItem
import com.example.common.navigation.NavigationBase
import com.example.common.refresh.RefreshVM
import com.example.design_system.components.bars.bottom_nav_bar.BottomNavBar
import com.example.favorites.navigation.favorites
import com.example.favorites.screen.FavoritesVM
import com.example.home.navigation.home
import com.example.home.screen.HomeVM
import com.example.more.navigation.more
import com.example.player.components.FullScreenPlayer
import com.example.player.components.PlayerContainer
import com.example.player.player.PlayerState
import com.example.player.player.PlayerVM

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    // Initialize vm's here to don't refetch values
    // Screens vm's
    val homeVM = hiltViewModel<HomeVM>()
    val favoritesVM = hiltViewModel<FavoritesVM>()
    val collectionsVM = hiltViewModel<CollectionsVM>()

    // Refresh vm
    val refreshVM = hiltViewModel<RefreshVM>()

    // Player vm
    val playerVM = hiltViewModel<PlayerVM>()

    // Current nav destination
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.currentNavBarRoute()

    SharedTransitionLayout {
        Box(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = HomeRoute
            ) {
                home(homeVM, navController)
                favorites(favoritesVM, refreshVM, navController)
                collections(collectionsVM, refreshVM, navController)
                animeDetails(refreshVM, playerVM, navController)
                more(navController)
            }

            val navBarVisible = selectedRoute is NavBarItem
            BottomNavBar(
                visible = navBarVisible,
                selectedRoute = selectedRoute,
                onNavItemClick = { route ->
                    navController.navigate(route) {
                        popUpTo(HomeRoute) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            val playerState by playerVM.playerState.collectAsStateWithLifecycle()
            AnimatedContent(
                targetState = playerState.playerState,
                transitionSpec = {
                    fadeIn(tween(500)) togetherWith fadeOut(tween(500))
                },
                label = "PlayerTransition"
            ) { targetState ->
                when (targetState) {
                    PlayerState.PlayerState.Full -> {
                        FullScreenPlayer(
                            player = playerVM.player,
                            playerState = playerState,
                            onPlayerEffect = playerVM::sendEffect,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    }

                    PlayerState.PlayerState.Mini -> {
                        PlayerContainer(
                            player = playerVM.player,
                            navBarVisible = navBarVisible,
                            playerState = playerState,
                            playerEffects = playerVM.playerEffects,
                            onPlayerEffect = playerVM::sendEffect,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    }

                    PlayerState.PlayerState.Closed -> {}
                }
            }
        }
    }
}

private fun NavBackStackEntry.currentNavBarRoute(): NavigationBase? =
    when {
        destination.hasRoute<HomeRoute>() -> HomeRoute
        destination.hasRoute<FavoritesRoute>() -> FavoritesRoute
        destination.hasRoute<CollectionsRoute>() -> CollectionsRoute
        destination.hasRoute<MoreRoute>() -> MoreRoute
        else -> null // Others
    }