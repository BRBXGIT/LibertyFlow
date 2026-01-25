package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class PosterDto(
    @SerializedName("optimized") val optimizedDto: OptimizedDto
)