package com.brbx.libertyflow.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.brbx.design_system.component.bar.nav_bar.composable.NavBar
import com.brbx.design_system.component.bar.nav_bar.model.NavBarDestination
import com.brbx.design_system.component.bar.nav_bar.model.NavBarItemModel
import com.brbx.home.navigation.HomeRoute
import com.brbx.home.navigation.home
import com.brbx.ui_compose.theme.mColors

@Composable
internal fun NavGraph() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = mColors.background)
    ) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = HomeRoute,
        ) {
            home(navController)
        }

        val currentDestination = navController.currentBackStackEntry?.destination
        val selectedDestination = rememberNavBarDestination(navItems = navBarItems, currentDestination)
        NavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            navItems = navBarItems,
            selectedDestination = selectedDestination,
            onItemClick = { route -> navController.navigate(route) },
        )
    }
}

@Composable
private fun rememberNavBarDestination(
    navItems: List<NavBarItemModel>,
    currentDestination: NavDestination?,
): NavBarDestination? = remember(key1 = navItems, key2 = currentDestination) {
    navItems.find { item ->
        currentDestination?.hasRoute(item.destination::class) == true
    }?.destination
}

