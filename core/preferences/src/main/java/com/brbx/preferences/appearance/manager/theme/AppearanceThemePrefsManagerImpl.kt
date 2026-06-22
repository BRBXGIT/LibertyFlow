package com.brbx.preferences.appearance.manager.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class AppearanceThemePrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), AppearanceThemePrefsManager {

    override val theme: Flow<String?> = getValue(ThemeKey)

    override suspend fun saveTheme(theme: String) {
        setValue(ThemeKey, theme)
    }

    private companion object {
        val ThemeKey = stringPreferencesKey(name = "liberty_flow_theme")
    }
}