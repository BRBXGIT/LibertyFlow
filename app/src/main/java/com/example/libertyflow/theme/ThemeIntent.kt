package com.example.libertyflow.theme

sealed interface ThemeIntent {
    data class ObserveTheme(val isSystemInDarkMode: Boolean): ThemeIntent
}