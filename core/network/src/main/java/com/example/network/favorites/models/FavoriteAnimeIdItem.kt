package com.example.network.favorites.models

import com.google.gson.annotations.SerializedName

data class FavoriteAnimeIdItem(
    @SerializedName("release_id") val releaseId: Int
)
