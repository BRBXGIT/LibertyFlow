package com.example.network.auth.models

import com.google.gson.annotations.SerializedName

data class SessionTokenResponseDto(
    @SerializedName("token") val token: String?,
    @SerializedName("error") val error: String,
)
