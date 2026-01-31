package com.example.network.common.common_pagination.meta


import com.google.gson.annotations.SerializedName

data class LinksDto(
    @SerializedName("next") val next: String
)