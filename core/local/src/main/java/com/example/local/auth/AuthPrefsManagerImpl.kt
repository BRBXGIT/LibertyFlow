package com.example.local.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class AuthPrefsManagerImpl(
    private val context: Context
): AuthPrefsManager {

    private val Context.dataStore by preferencesDataStore("liberty_fow_auth_prefs")

    private companion object {
        private val USER_SESSION_TOKEN_KEY = stringPreferencesKey("user_session_token")
    }

    override val token = context.dataStore.data
        .map { preferences -> preferences[USER_SESSION_TOKEN_KEY] }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_SESSION_TOKEN_KEY] = token
        }
    }

    override suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_SESSION_TOKEN_KEY)
        }
    }
}