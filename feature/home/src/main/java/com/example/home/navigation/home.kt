package com.example.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.HomeRoute
import com.example.home.screen.Home

fun NavGraphBuilder.home() = composable<HomeRoute> {
    Home()
}