package com.example.common.ui_helpers.effects

import com.example.common.navigation.NavigationBase
import android.content.Intent

/**
 * Defines one-time UI events that are not part of the persistent state.
 * [UiEffect] represents 'what just happened' (e.g., navigation, showing a snackbar).
 */
sealed interface UiEffect {

    // Snackbars
    data class ShowSnackbarWithAction(
        val messageRes: Int,
        val actionLabel: String? = null,
        val action: (() -> Unit)? = null,
    ): UiEffect
    data class ShowSimpleSnackbar(val messageRes: Int): UiEffect

    // Navigation
    data class Navigate(val route: NavigationBase): UiEffect
    data object NavigateBack: UiEffect

    // Intents
    data class IntentTo(val intent: Intent): UiEffect
}