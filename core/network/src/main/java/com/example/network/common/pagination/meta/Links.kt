package com.example.network.common.pagination.meta


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("next") val next: String
)