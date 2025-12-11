package com.example.common

import com.example.common.navigation.HomeRoute
import com.example.common.navigation.NavigationBase

data class UiState(
    val selectedRoute: NavigationBase = HomeRoute
) {
    fun setRoute(route: NavigationBase) = copy(selectedRoute = route)
}
