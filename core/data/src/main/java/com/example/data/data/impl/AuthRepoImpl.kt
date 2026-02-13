package com.example.data.data.impl

import com.example.data.domain.AuthRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.Token
import com.example.data.models.auth.TokenRequest
import com.example.data.models.auth.toSessionTokenRequestDto
import com.example.data.models.auth.toToken
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.local.auth.AuthPrefsManager
import com.example.network.auth.api.AuthApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authPrefsManager: AuthPrefsManager
): AuthRepo {

    private companion object {
        const val TOKEN_TYPE = "Bearer"
    }

    override val token = authPrefsManager.token

    override val authState = token.map { token ->
        if (token.isNullOrBlank()) AuthState.LoggedOut else AuthState.LoggedIn
    }

    override suspend fun getToken(request: TokenRequest): NetworkResult<Token> {
        return NetworkRequest.safeApiCall(
            call = { authApi.getSessionToken(request.toSessionTokenRequestDto()) },
            map = { it.toToken() }
        )
    }

    override suspend fun logout(): NetworkResult<Unit> {
        return NetworkRequest.safeApiCall(
            call = { authApi.logout(token.firstOrNull()) },
            map = {}
        )
    }

    override suspend fun clearToken() = authPrefsManager.clearToken()

    override suspend fun saveToken(token: String) = authPrefsManager.saveToken("$TOKEN_TYPE $token")
}