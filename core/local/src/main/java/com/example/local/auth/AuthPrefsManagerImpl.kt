package com.example.local.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// TODO: Rewrite to encrypted
private const val DATASTORE_NAME = "liberty_fow_auth_prefs"
private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

/**
 * Manages the persistence of authentication credentials using Jetpack DataStore.
 * Provides a reactive stream for the session token and methods for lifecycle management.
 */
@Singleton
class AuthPrefsManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
): AuthPrefsManager {

    private companion object {
        private val USER_SESSION_TOKEN_KEY = stringPreferencesKey("user_session_token")
    }

    /**
     * A cold [Flow] that emits the current session token.
     * Emits `null` if no token is found or if the session has been cleared.
     */
    override val token = context.dataStore.data
        .map { preferences ->
            preferences[USER_SESSION_TOKEN_KEY]
        }

    /**
     * Persists a new session token.
     * @param token The raw token string (Bearer token).
     */
    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_SESSION_TOKEN_KEY] = token
        }
    }

    /**
     * Removes the session token from local storage.
     * Use this during logout or when a token is found to be expired.
     */
    override suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_SESSION_TOKEN_KEY)
        }
    }
}