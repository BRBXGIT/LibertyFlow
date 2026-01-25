package com.example.network.auth.models

import com.google.gson.annotations.SerializedName

data class SessionTokenRequestDto(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)
