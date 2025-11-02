package com.example.network.common.anime_item_response

import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("main")
    val main: String
)