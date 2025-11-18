package com.example.common

import com.example.common.navigation.NavigationBase

sealed interface UiIntent {
    data class ChangeSelectedRoute(val route: NavigationBase): UiIntent
}