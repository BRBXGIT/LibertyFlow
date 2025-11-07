package com.example.libertyflow.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.common.navigation.HomeRoute
import com.example.design_system.components.bottom_nav_bar.BottomNavBar
import com.example.home.navigation.home

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    Box {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            home()
        }

        BottomNavBar(
            visible = true,
            onNavItemClick = { _, _ -> },
            selectedIndex = 1
        )
    }
}