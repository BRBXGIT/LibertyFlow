package com.example.network.favorites.models

import com.google.gson.annotations.SerializedName

data class FavoriteAnimeIdItemDto(
    @SerializedName("release_id") val releaseId: Int
)
