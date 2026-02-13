package com.example.local.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.local.utils.BasePrefsManager
import com.example.local.utils.DataStoreQualifier
import com.example.local.utils.LibertyFlowDataStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the state of the theme and color scheme settings.
 */
class ThemePrefsManagerImpl @Inject constructor(
    @DataStoreQualifier(LibertyFlowDataStore.Theme) dataStore: DataStore<Preferences>
): ThemePrefsManager, BasePrefsManager(dataStore) {

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
}