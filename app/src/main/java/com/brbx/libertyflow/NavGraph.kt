package com.brbx.libertyflow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.brbx.home.navigation.HomeRoute
import com.brbx.home.navigation.home

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        home(navController)
    }
}