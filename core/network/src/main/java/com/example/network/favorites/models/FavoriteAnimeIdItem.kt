package com.example.network.favorites.models

import com.example.network.common.anime_base.AnimeBase
import com.google.gson.annotations.SerializedName

data class FavoriteAnimeIdItem(
    @SerializedName("release_id") override val id: Int
): AnimeBase
