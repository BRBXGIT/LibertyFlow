package com.example.more.screen

/**
 * Represents the set of possible user intentions or actions on the 'More' screen.
 * * Using a sealed interface ensures that the [MoreVM] handles every possible
 * interaction through an exhaustive `when` expression.
 */
sealed interface MoreIntent {
    // Ui
    data object ToggleLogoutDialog: MoreIntent

    // Data
    data object Logout: MoreIntent
}