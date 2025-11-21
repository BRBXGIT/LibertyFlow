package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class AnimeResponseItem(
    @SerializedName("id") val id: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("name") val name: Name,
    @SerializedName("poster") val poster: Poster
)