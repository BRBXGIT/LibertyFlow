package com.example.data.models.auth

import com.example.network.auth.models.SessionTokenRequest
import com.example.network.auth.models.SessionTokenResponse

internal fun UiTokenRequest.toSessionTokenRequest(): SessionTokenRequest {
    return SessionTokenRequest(
        login = login,
        password = password
    )
}

internal fun SessionTokenResponse.toUiToken(): UiToken {
    return UiToken(token = token)
}