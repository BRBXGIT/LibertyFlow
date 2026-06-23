package com.brbx.preferences.player.manager.opening

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class PlayerOpeningPrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), PlayerOpeningPrefsManager {

    override val showSkipButton: Flow<Boolean?> = getValue(ShowSkipButtonKey)
    override val autoSkip: Flow<Boolean?> = getValue(AutoSkipKey)

    override suspend fun saveShowSkipButton(showSkipButton: Boolean) {
        setValue(ShowSkipButtonKey, showSkipButton)
    }

    override suspend fun saveAutoSkip(autoSkip: Boolean) {
        setValue(AutoSkipKey, autoSkip)
    }

    private companion object {
        val ShowSkipButtonKey = booleanPreferencesKey(name = "liberty_flow_player_show_skip_opening_button")
        val AutoSkipKey = booleanPreferencesKey(name = "liberty_flow_player_auto_opening_skip")
    }
}