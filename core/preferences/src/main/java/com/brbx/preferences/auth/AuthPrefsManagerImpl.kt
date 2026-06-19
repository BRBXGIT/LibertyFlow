package com.brbx.preferences.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brbx.preferences.base.BasePrefsManager
import kotlinx.coroutines.flow.Flow

internal class AuthPrefsManagerImpl(
    dataStore: DataStore<Preferences>
) : BasePrefsManager(dataStore), AuthPrefsManager {

    override val token: Flow<String?>
        get() = getValue(SessionTokenKey)

    override suspend fun saveToken(token: String) {
        setValue(SessionTokenKey, token)
    }

    override suspend fun clearToken() {
        clearValue(SessionTokenKey)
    }

    private companion object {
        val SessionTokenKey = stringPreferencesKey(name = "liberty_flow_session_token")
    }
}