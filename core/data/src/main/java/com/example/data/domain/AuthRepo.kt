package com.example.data.domain

import com.example.data.models.auth.AuthState
import com.example.data.models.auth.UiToken
import com.example.data.models.auth.UiTokenRequest
import com.example.data.utils.remote.network_request.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    val authState: Flow<AuthState>

    val token: Flow<String?>

    suspend fun getToken(request: UiTokenRequest): NetworkResult<UiToken>

    suspend fun saveToken(token: String)

    suspend fun clearToken()
}