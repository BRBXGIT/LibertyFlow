@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.libertyflow.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.design_system.components.bars.bottom_nav_bar.LibertyFlowBottomNavBar
import com.example.design_system.theme.logic.ThemeVM
import com.example.favorites.navigation.favorites
import com.example.favorites.screen.FavoritesVM
import com.example.home.navigation.home
import com.example.home.screen.HomeVM
import com.example.info.navigation.info
import com.example.more.navigation.more
import com.example.onboarding.navigation.onboarding
import com.example.player.player.PlayerContainer
import com.example.player.player.PlayerVM
import com.example.settings.navigation.settings

@Composable
fun NavGraph(themeVM: ThemeVM, startDestination: NavigationBase) {
    val navController = rememberNavController()

    // Initialize vm's here to don't refetch values
    // Screens vm's
    val homeVM = hiltViewModel<HomeVM>()
    val favoritesVM = hiltViewModel<FavoritesVM>()
    val collectionsVM = hiltViewModel<CollectionsVM>()

    // Refresh vm
    val refreshVM = viewModel<RefreshVM>()

    // Player vm
    val playerVM = hiltViewModel<PlayerVM>()

    // Current nav destination
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.currentNavBarRoute()

    SharedTransitionLayout {
        Box(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                onboarding(navController)
                home(homeVM, navController)
                favorites(favoritesVM, refreshVM, navController)
                collections(collectionsVM, refreshVM, themeVM, navController)
                animeDetails(refreshVM, playerVM, navController)
                more(navController)
                info(navController)
                settings(navController)
            }

            val navBarVisible = selectedRoute is NavBarItem
            LibertyFlowBottomNavBar(
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
            PlayerContainer(
                playerEffects = playerVM.playerEffects,
                playerState = playerState,
                player = playerVM.uiPlayer,
                navBarVisible = navBarVisible,
                onPlayerEffect = playerVM::sendEffect,
                onPlayerIntent = playerVM::sendIntent
            )
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