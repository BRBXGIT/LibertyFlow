package com.brbx.preferences.player.manager.fullscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerFullScreenPrefsManagerImpl(
    dataStore: DataStore<Preferences>,
) : BasePrefsManager(dataStore), PlayerFullScreenPrefsManager {

    override val fullScreen: Flow<Boolean?> = getValue(FullScreenKey)

    override suspend fun saveFullScreen(fullScreen: Boolean) {
        setValue(FullScreenKey, fullScreen)
    }

    private companion object {
        val FullScreenKey = booleanPreferencesKey(name = "liberty_flow_player_full_screen")
    }
}