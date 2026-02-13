package com.example.local.player_settings

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

private const val DATASTORE_NAME = "liberty_flow_player_prefs"
private val Context.datastore by preferencesDataStore(DATASTORE_NAME)

/**
 * Manages the state of the player settings.
 */
@Singleton
class PlayerPrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): PlayerPrefsManager {
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

    // Helpers
    private fun <T> getValue(key: Preferences.Key<T>): Flow<T?> {
        return context.datastore.data
            .map { preferences -> preferences[key] }
    }

    private suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        context.datastore.edit { preferences ->
            preferences[key] = value
        }
    }
}