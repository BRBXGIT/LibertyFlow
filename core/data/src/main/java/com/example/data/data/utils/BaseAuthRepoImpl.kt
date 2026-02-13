package com.example.data.data.utils

import com.example.local.auth.AuthPrefsManager
import kotlinx.coroutines.flow.filterNotNull

/**
 * Base repository providing authenticated session management.
 * * [token] filters for non-null session tokens to ensure downstream
 * API calls are only executed when token is exists.
 */
abstract class BaseAuthRepoImpl(authPrefsManager: AuthPrefsManager) {
    /**
     * A Flow of validated session tokens used for authenticated requests.
     */
    protected val token = authPrefsManager.token.filterNotNull()
}