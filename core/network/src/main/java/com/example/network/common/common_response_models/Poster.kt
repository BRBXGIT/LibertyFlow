package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class Poster(
    @SerializedName("optimized") val optimized: Optimized
)