package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class Poster(
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("preview") val preview: String,
    @SerializedName("src") val src: String
)