package com.example.libertyflow.theme

sealed interface ThemeIntent {
    data class UpdateSystemDarkMode(val isSystemInDarkMode: Boolean) : ThemeIntent
}