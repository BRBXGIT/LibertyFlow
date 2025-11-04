package com.example.network.releases.models.anime_details_item_response


import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("ending") val ending: Ending,
    @SerializedName("hls_1080") val hls1080: String?,
    @SerializedName("hls_480") val hls480: String,
    @SerializedName("hls_720") val hls720: String,
    @SerializedName("name") val name: String?,
    @SerializedName("opening") val opening: Opening
)