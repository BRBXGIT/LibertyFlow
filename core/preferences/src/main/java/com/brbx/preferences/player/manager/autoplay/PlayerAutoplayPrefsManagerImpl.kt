package com.brbx.preferences.player.manager.autoplay

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerAutoplayPrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), PlayerAutoplayPrefsManager {

    override val value: Flow<Boolean?> = getValue(AutoPlayKey)

    override suspend fun save(value: Boolean?) {
        setValue(AutoPlayKey, value)
    }

    private companion object {
        val AutoPlayKey = booleanPreferencesKey(name = "liberty_flow_player_auto_play")
    }
}