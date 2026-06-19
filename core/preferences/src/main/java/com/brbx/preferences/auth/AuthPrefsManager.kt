package com.brbx.preferences.auth

import kotlinx.coroutines.flow.Flow

interface AuthPrefsManager {

    val token: Flow<String?>

    suspend fun saveToken(token: String)
    suspend fun clearToken()
}