package com.example.local.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemePrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): ThemePrefsManager {

    private companion object {
        private const val DATASTORE_NAME = "liberty_flow_theme_prefs"

        private const val THEME_KEY_NAME = "theme"
        private const val COLOR_SYSTEM_KEY_NAME = "color_system"
        private const val USE_EXPRESSIVE_KEY_NAME = "use_expressive"

        private val THEME_KEY = stringPreferencesKey(THEME_KEY_NAME)
        private val COLOR_SYSTEM_KEY = stringPreferencesKey(COLOR_SYSTEM_KEY_NAME)
        private val USE_EXPRESSIVE_KEY = booleanPreferencesKey(USE_EXPRESSIVE_KEY_NAME)
    }

    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

    override val theme = context.dataStore.data
        .map { preferences -> preferences[THEME_KEY] }

    override val colorSystem = context.dataStore.data
        .map { preferences -> preferences[COLOR_SYSTEM_KEY] }

    override val useExpressive = context.dataStore.data
        .map { preferences -> preferences[USE_EXPRESSIVE_KEY] }

    override suspend fun saveColorSystem(system: String) {
        context.dataStore.edit { preferences ->
            preferences[COLOR_SYSTEM_KEY] = system
        }
    }

    override suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    override suspend fun saveUseExpressive(use: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USE_EXPRESSIVE_KEY] = use
        }
    }
}