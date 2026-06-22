package com.brbx.preferences.appearance.manager.color_scheme

import kotlinx.coroutines.flow.Flow

interface AppearanceColorSchemePrefsManager {

    val colorScheme: Flow<String?>

    suspend fun saveColorScheme(colorScheme: String)
}