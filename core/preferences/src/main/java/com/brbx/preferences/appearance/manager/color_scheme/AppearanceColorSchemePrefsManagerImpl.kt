package com.brbx.preferences.appearance.manager.color_scheme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class AppearanceColorSchemePrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), AppearanceColorSchemePrefsManager {

    override val colorScheme: Flow<String?> = getValue(ColorSchemeKey)

    override suspend fun saveColorScheme(colorScheme: String) {
        setValue(ColorSchemeKey, colorScheme)
    }

    private companion object {
        val ColorSchemeKey = stringPreferencesKey(name = "liberty_flow_color_scheme")
    }
}