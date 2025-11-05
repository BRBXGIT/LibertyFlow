package com.example.data.domain

import com.example.data.models.auth.UiToken
import com.example.data.models.auth.UiTokenRequest
import com.example.data.utils.remote.network_request.NetworkResult

interface AuthRepo {
    
    suspend fun getToken(request: UiTokenRequest): NetworkResult<UiToken>
}