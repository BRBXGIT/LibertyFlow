package com.example.data.data

import com.example.data.domain.AuthRepo
import com.example.data.models.auth.UiToken
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.auth.toSessionTokenRequest
import com.example.data.models.auth.toUiToken
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.network.auth.api.AuthApi
import javax.inject.Inject

// TODO ADD LOCAL DATASTORE
class AuthRepoImpl @Inject constructor(
    private val authApi: AuthApi
): AuthRepo {

    override suspend fun getToken(request: UiTokenRequest): NetworkResult<UiToken> {
        return NetworkRequest.safeApiCall(
            call = { authApi.getSessionToken(request.toSessionTokenRequest()) },
            map = { it.toUiToken() }
        )
    }
}