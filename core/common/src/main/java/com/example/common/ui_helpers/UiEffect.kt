package com.example.common.ui_helpers

import com.example.common.navigation.NavigationBase

sealed interface UiEffect {
    data class ShowSnackbar(
        val messageRes: Int,
        val actionLabel: String? = null,
        val action: (() -> Unit)? = null,
    ): UiEffect

    data class Navigate(val route: NavigationBase): UiEffect

    data object NavigateBack: UiEffect
}