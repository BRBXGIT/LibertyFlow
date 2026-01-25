package com.example.data.models.auth

data class TokenRequest(
    val login: String,
    val password: String
)