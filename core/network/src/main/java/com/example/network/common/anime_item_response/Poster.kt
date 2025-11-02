package com.example.network.common.anime_item_response

import com.google.gson.annotations.SerializedName

data class Poster(
    @SerializedName("thumbnail")
    val thumbnail: String
)