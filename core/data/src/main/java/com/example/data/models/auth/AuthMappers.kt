package com.example.data.models.auth

import com.example.network.auth.models.SessionTokenRequest
import com.example.network.auth.models.SessionTokenResponse

fun UiTokenRequest.sessionTokenRequest(): SessionTokenRequest {
    return SessionTokenRequest(
        login = login,
        password = password
    )
}

fun SessionTokenResponse.toUiToken(): UiToken {
    return UiToken(token = token)
}