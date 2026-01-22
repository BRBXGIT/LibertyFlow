package com.example.local.player_settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerPrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): PlayerPrefsManager {
    private companion object {
        private const val DATASTORE_NAME = "liberty_flow_player_prefs"

        private const val QUALITY_KEY_NAME = "quality"
        private const val SHOW_SKIP_OPENING_BUTTON_KEY_NAME = "show_skip_opening_button"
        private const val AUTO_SKIP_OPENING_KEY_NAME = "auto_skip_opening"
        private const val AUTO_PLAY_KEY_NAME = "auto_play"

        private val QUALITY_KEY = stringPreferencesKey(QUALITY_KEY_NAME)
        private val SHOW_SKIP_OPENING_BUTTON_KEY = booleanPreferencesKey(SHOW_SKIP_OPENING_BUTTON_KEY_NAME)
        private val AUTO_SKIP_OPENING_KEY = booleanPreferencesKey(AUTO_SKIP_OPENING_KEY_NAME)
        private val AUTO_PLAY_KEY = booleanPreferencesKey(AUTO_PLAY_KEY_NAME)
    }

    private val Context.datastore by preferencesDataStore(DATASTORE_NAME)

    override val quality = context.datastore.data
        .map { preferences -> preferences[QUALITY_KEY] }

    override val showSkipOpeningButton = context.datastore.data
        .map { preferences -> preferences[SHOW_SKIP_OPENING_BUTTON_KEY] }

    override val autoSkipOpening = context.datastore.data
        .map { preferences -> preferences[AUTO_SKIP_OPENING_KEY] }

    override val autoPlay = context.datastore.data
        .map { preferences -> preferences[AUTO_PLAY_KEY] }

    override suspend fun saveQuality(quality: String) {
        context.datastore.edit { preferences ->
            preferences[QUALITY_KEY] = quality
        }
    }

    override suspend fun saveShowSkipOpeningButton(show: Boolean) {
        context.datastore.edit { preferences ->
            preferences[SHOW_SKIP_OPENING_BUTTON_KEY] = show
        }
    }

    override suspend fun saveAutoSkipOpening(skip: Boolean) {
        context.datastore.edit { preferences ->
            preferences[AUTO_SKIP_OPENING_KEY] = skip
        }
    }

    override suspend fun saveAutoPlay(autoPlay: Boolean) {
        context.datastore.edit { preferences ->
            preferences[AUTO_PLAY_KEY] = autoPlay
        }
    }
}