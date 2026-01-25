package com.example.data.models.auth

import com.example.network.auth.models.SessionTokenRequestDto
import com.example.network.auth.models.SessionTokenResponseDto

internal fun TokenRequest.toSessionTokenRequestDto(): SessionTokenRequestDto {
    return SessionTokenRequestDto(
        login = login,
        password = password
    )
}

internal fun SessionTokenResponseDto.toToken(): Token {
    return Token(token = token)
}