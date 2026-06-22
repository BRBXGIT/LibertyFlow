package com.brbx.preferences.player.manager.auto_play

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerAutoPlayPrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), PlayerAutoPlayPrefsManager {

    override val autoPlay: Flow<Boolean?> = getValue(AutoPlayKey)

    override suspend fun saveAutoPlay(autoPlay: Boolean) {
        setValue(AutoPlayKey, autoPlay)
    }

    private companion object {
        val AutoPlayKey = booleanPreferencesKey(name = "liberty_flow_player_auto_play")
    }
}