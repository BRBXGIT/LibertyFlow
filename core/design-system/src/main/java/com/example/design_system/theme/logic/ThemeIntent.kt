package com.example.design_system.theme.logic

sealed interface ThemeIntent {
    data class UpdateSystemDarkMode(val isSystemInDarkMode: Boolean) : ThemeIntent
}