package com.brbx.preferences.player.manager.quality

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerQualityPrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), PlayerQualityPrefsManager {

    override val value: Flow<String?> = getValue(QualityKey)

    override suspend fun save(value: String) {
        setValue(QualityKey, value)
    }

    private companion object {
        val QualityKey = stringPreferencesKey(name = "liberty_flow_player_quality")
    }
}