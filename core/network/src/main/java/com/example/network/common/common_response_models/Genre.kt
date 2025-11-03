package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int
)