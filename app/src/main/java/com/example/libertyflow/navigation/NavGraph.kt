package com.example.libertyflow.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.common.UiIntent
import com.example.common.UiVM
import com.example.common.navigation.HomeRoute
import com.example.design_system.components.bottom_nav_bar.BottomNavBar
import com.example.home.navigation.home

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    val uiVM = viewModel<UiVM>()

    Box {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.align(Alignment.Center)
        ) {
            home()
        }

        val uiState by uiVM.uiState.collectAsStateWithLifecycle()
        BottomNavBar(
            selectedRoute = uiState.selectedRoute,
            onNavItemClick = { uiVM.sendIntent(UiIntent.ChangeSelectedRoute(it)) }
        )
    }
}