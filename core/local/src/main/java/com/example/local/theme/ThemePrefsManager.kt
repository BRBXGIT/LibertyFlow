package com.example.local.theme

import kotlinx.coroutines.flow.Flow

interface ThemePrefsManager {

    val theme: Flow<String?>

    val colorSystem: Flow<String?>

    val useExpressive: Flow<Boolean?>

    suspend fun saveTheme(theme: String)

    suspend fun saveColorSystem(system: String)

    suspend fun saveUseExpressive(use: Boolean)
}