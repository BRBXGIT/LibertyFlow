package com.example.network.common.common_request_models.request_parameters

import com.google.gson.annotations.SerializedName

data class YearsDto(
    @SerializedName("from_year") val fromYear: Int,
    @SerializedName("to_year") val toYear: Int
)