package com.example.data.data

import com.example.data.domain.AuthRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.UiToken
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.auth.toSessionTokenRequest
import com.example.data.models.auth.toUiToken
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.local.auth.AuthConstants
import com.example.local.auth.AuthPrefsManager
import com.example.network.auth.api.AuthApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authPrefsManager: AuthPrefsManager
): AuthRepo {

    override val token = authPrefsManager.token

    override val authState = token.map { token ->
        if (token.isNullOrBlank()) AuthState.LoggedOut else AuthState.LoggedIn
    }

    override suspend fun getToken(request: UiTokenRequest): NetworkResult<UiToken> {
        return NetworkRequest.safeApiCall(
            call = { authApi.getSessionToken(request.toSessionTokenRequest()) },
            map = { it.toUiToken() }
        )
    }

    override suspend fun clearToken() = authPrefsManager.clearToken()

    override suspend fun saveToken(token: String) = authPrefsManager.saveToken("${AuthConstants.TOKEN_TYPE} $token")
}