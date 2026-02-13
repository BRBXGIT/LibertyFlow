package com.example.data.data.utils

import com.example.local.auth.AuthPrefsManager
import kotlinx.coroutines.flow.filterNotNull

abstract class BaseAuthRepoImpl(authPrefsManager: AuthPrefsManager) {
    protected val token = authPrefsManager.token.filterNotNull()
}