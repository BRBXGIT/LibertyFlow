package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class NameDto(
    @SerializedName("main") val main: String,
    @SerializedName("english") val english: String,
    @SerializedName("alternative") val alternative: String?
)