package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class OptimizedDto(
    @SerializedName("src") val src: String,
    @SerializedName("preview") val preview: String,
    @SerializedName("thumbnail") val thumbnail: String
)
