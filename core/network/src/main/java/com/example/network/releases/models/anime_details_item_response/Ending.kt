package com.example.network.releases.models.anime_details_item_response


import com.google.gson.annotations.SerializedName

data class Ending(
    @SerializedName("start")
    val start: Any?,
    @SerializedName("stop")
    val stop: Any?
)