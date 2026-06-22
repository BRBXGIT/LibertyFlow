package com.brbx.preferences.appearance.manager.theme

import kotlinx.coroutines.flow.Flow

interface AppearanceThemePrefsManager {

    val theme: Flow<String?>

    suspend fun saveTheme(theme: String)
}