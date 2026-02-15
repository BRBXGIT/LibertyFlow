package com.example.design_system.theme.logic

/**
 * Defines the user actions or events that can modify the application's theme state.
 */
sealed interface ThemeIntent {
    data class UpdateSystemDarkMode(val isSystemInDarkMode: Boolean) : ThemeIntent
}