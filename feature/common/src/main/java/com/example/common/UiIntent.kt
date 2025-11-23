package com.example.common

import com.example.common.navigation.NavigationBase

sealed interface UiIntent {
    data class UpdateSelectedRoute(val route: NavigationBase): UiIntent
}