package com.example.more.screen

sealed interface MoreIntent {
    // Ui
    data object ToggleLogoutDialog: MoreIntent

    // Data
    data object Logout: MoreIntent
}