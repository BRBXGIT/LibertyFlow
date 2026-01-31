package com.example.local.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Singleton DataStore instance to prevent multiple open files
private const val DATA_STORE_NAME = "liberty_flow_theme_prefs"
private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

@Singleton
class ThemePrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ThemePrefsManager {

    private companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val COLOR_SYSTEM_KEY = stringPreferencesKey("color_system")
        val USE_EXPRESSIVE_KEY = booleanPreferencesKey("use_expressive")
    }

    override val theme: Flow<String?> = context.dataStore.data
        .map { it[THEME_KEY] }

    override val colorSystem: Flow<String?> = context.dataStore.data
        .map { it[COLOR_SYSTEM_KEY] }

    override val useExpressive: Flow<Boolean?> = context.dataStore.data
        .map { it[USE_EXPRESSIVE_KEY] }

    override suspend fun saveTheme(theme: String) {
        context.dataStore.edit { it[THEME_KEY] = theme }
    }

    override suspend fun saveColorSystem(system: String) {
        context.dataStore.edit { it[COLOR_SYSTEM_KEY] = system }
    }

    override suspend fun saveUseExpressive(use: Boolean) {
        context.dataStore.edit { it[USE_EXPRESSIVE_KEY] = use }
    }
}