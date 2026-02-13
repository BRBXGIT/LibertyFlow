package com.example.local.player_settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.local.utils.BasePrefsManager
import com.example.local.utils.PlayerSettingsDataStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the state of the player settings.
 */
@Singleton
class PlayerSettingsPrefsManagerImpl @Inject constructor(
    @PlayerSettingsDataStore dataStore: DataStore<Preferences>
): PlayerSettingsPrefsManager, BasePrefsManager(dataStore) {
    private companion object {
        private val QUALITY_KEY = stringPreferencesKey("quality")
        private val SHOW_SKIP_OPENING_BUTTON_KEY = booleanPreferencesKey("show_skip_opening_button")
        private val AUTO_SKIP_OPENING_KEY = booleanPreferencesKey("auto_skip_opening")
        private val AUTO_PLAY_KEY = booleanPreferencesKey("auto_play")
        private val IS_CROPPED_KEY = booleanPreferencesKey("is_cropped")
    }

    // --- Observable Data Streams ---
    override val quality = getValue(QUALITY_KEY)

    override val showSkipOpeningButton = getValue(SHOW_SKIP_OPENING_BUTTON_KEY)

    override val autoSkipOpening = getValue(AUTO_SKIP_OPENING_KEY)

    override val autoPlay = getValue(AUTO_PLAY_KEY)

    override val isCropped = getValue(IS_CROPPED_KEY)

    // --- Write operations ---
    override suspend fun saveQuality(quality: String) =
        setValue(QUALITY_KEY, quality)

    override suspend fun saveShowSkipOpeningButton(show: Boolean) =
        setValue(SHOW_SKIP_OPENING_BUTTON_KEY, show)

    override suspend fun saveAutoSkipOpening(skip: Boolean) =
        setValue(AUTO_SKIP_OPENING_KEY, skip)

    override suspend fun saveAutoPlay(autoPlay: Boolean) =
        setValue(AUTO_PLAY_KEY, autoPlay)

    override suspend fun saveIsCopped(isCopped: Boolean) =
        setValue(IS_CROPPED_KEY, isCopped)
}