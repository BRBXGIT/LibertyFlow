package com.brbx.data.network.api.client_token_provider

import com.brbx.network.base.client.ApiClientTokenProvider
import com.brbx.preferences.auth.AuthPrefsManager
import kotlinx.coroutines.flow.first

internal class ApiClientTokenProviderImpl(
    private val prefs: AuthPrefsManager,
) : ApiClientTokenProvider {

    override suspend fun getToken(): String? =
        prefs.token.first()

    override suspend fun clearToken() {
        prefs.clearToken()
    }
}