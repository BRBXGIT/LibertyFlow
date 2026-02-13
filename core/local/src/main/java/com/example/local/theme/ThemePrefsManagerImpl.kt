package com.example.local.theme

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_NAME = "liberty_flow_theme_prefs"
private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

/**
 * Manages the state of the theme and color scheme settings.
 */
@Singleton
class ThemePrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): ThemePrefsManager {

    private companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val COLOR_SYSTEM_KEY = stringPreferencesKey("color_system")
        val USE_EXPRESSIVE_KEY = booleanPreferencesKey("use_expressive")
        val TAB_TYPE_KEY = stringPreferencesKey("tablet_type")
    }

    // --- Observable Data Streams ---
    override val theme = getValue(THEME_KEY)

    override val colorSystem = getValue(COLOR_SYSTEM_KEY)

    override val useExpressive = getValue(USE_EXPRESSIVE_KEY)

    override val tabType = getValue(TAB_TYPE_KEY)

    // --- Write operations ---
    override suspend fun saveTheme(theme: String) =
        setValue(THEME_KEY, theme)

    override suspend fun saveColorSystem(system: String) =
        setValue(COLOR_SYSTEM_KEY, system)

    override suspend fun saveUseExpressive(use: Boolean) =
        setValue(USE_EXPRESSIVE_KEY, use)

    override suspend fun saveTabletType(type: String) =
        setValue(TAB_TYPE_KEY, type)

    // Helpers
    private fun <T> getValue(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data
            .map { preferences -> preferences[key] }
    }

    private suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}