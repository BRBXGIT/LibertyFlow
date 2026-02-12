package com.example.local.player_settings

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
private const val DATASTORE_NAME = "liberty_flow_player_prefs"
private val Context.datastore by preferencesDataStore(DATASTORE_NAME)

@Singleton
class PlayerPrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): PlayerPrefsManager {
    private companion object {
        private val QUALITY_KEY = stringPreferencesKey("quality")
        private val SHOW_SKIP_OPENING_BUTTON_KEY = booleanPreferencesKey("show_skip_opening_button")
        private val AUTO_SKIP_OPENING_KEY = booleanPreferencesKey("auto_skip_opening")
        private val AUTO_PLAY_KEY = booleanPreferencesKey("auto_play")
        private val IS_CROPPED = booleanPreferencesKey("is_cropped")
    }

    override val quality = context.datastore.data
        .map { preferences -> preferences[QUALITY_KEY] }

    override val showSkipOpeningButton = context.datastore.data
        .map { preferences -> preferences[SHOW_SKIP_OPENING_BUTTON_KEY] }

    override val autoSkipOpening = context.datastore.data
        .map { preferences -> preferences[AUTO_SKIP_OPENING_KEY] }

    override val autoPlay = context.datastore.data
        .map { preferences -> preferences[AUTO_PLAY_KEY] }

    override val isCropped = context.datastore.data
        .map { preferences -> preferences[IS_CROPPED] }

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

    override suspend fun saveIsCopped(isCopped: Boolean) {
        context.datastore.edit { preferences ->
            preferences[IS_CROPPED] = isCopped
        }
    }
}