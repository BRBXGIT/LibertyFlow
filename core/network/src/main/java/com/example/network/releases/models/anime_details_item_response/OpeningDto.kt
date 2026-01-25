package com.example.network.releases.models.anime_details_item_response


import com.google.gson.annotations.SerializedName

data class OpeningDto(
    @SerializedName("start")
    val start: Int?,
    @SerializedName("stop")
    val stop: Int?
)