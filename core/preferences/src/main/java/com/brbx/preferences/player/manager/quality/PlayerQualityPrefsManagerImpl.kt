package com.brbx.preferences.player.manager.quality

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerQualityPrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), PlayerQualityPrefsManager {

    override val quality: Flow<String?> = getValue(QualityKey)

    override suspend fun saveQuality(quality: String) {
        setValue(QualityKey, quality)
    }

    private companion object {
        val QualityKey = stringPreferencesKey(name = "liberty_flow_player_quality")
    }
}