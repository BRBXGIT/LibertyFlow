package com.example.network.releases.models.anime_details_item_response


import com.google.gson.annotations.SerializedName

data class EndingDto(
    @SerializedName("start")
    val start: Int?,
    @SerializedName("stop")
    val stop: Int?
)