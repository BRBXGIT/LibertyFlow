package com.example.local.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.local.utils.AuthDataStore
import com.example.local.utils.BasePrefsManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// TODO: Rewrite to encrypted
/**
 * Manages the persistence of authentication credentials using Jetpack DataStore.
 * Provides a reactive stream for the session token and methods for lifecycle management.
 */
@Singleton
class AuthPrefsManagerImpl @Inject constructor(
    @AuthDataStore dataStore: DataStore<Preferences>
): AuthPrefsManager, BasePrefsManager(dataStore) {

    private companion object {
        private val USER_SESSION_TOKEN_KEY = stringPreferencesKey("user_session_token")
    }

    /**
     * A cold [Flow] that emits the current session token.
     * Emits `null` if no token is found or if the session has been cleared.
     */
    override val token = getValue(USER_SESSION_TOKEN_KEY)

    /**
     * Persists a new session token.
     * @param token The raw token string (Bearer token).
     */
    override suspend fun saveToken(token: String) =
        setValue(USER_SESSION_TOKEN_KEY, token)

    /**
     * Removes the session token from local storage.
     * Use this during logout or when a token is found to be expired.
     */
    override suspend fun clearToken() =
        clearValue(USER_SESSION_TOKEN_KEY)
}