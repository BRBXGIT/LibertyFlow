package com.example.local.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// TODO rewrite to encrypted
class AuthPrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): AuthPrefsManager {

    private companion object {
        private const val DATASTORE_NAME = "liberty_fow_auth_prefs"

        private const val USER_SESSION_TOKEN_KEY_NAME = "user_session_token"

        private val USER_SESSION_TOKEN_KEY = stringPreferencesKey(USER_SESSION_TOKEN_KEY_NAME)
    }

    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

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