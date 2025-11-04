package com.example.network.common.common_response_models

import com.example.network.common.anime_item_base.AnimeItemBase
import com.google.gson.annotations.SerializedName

data class AnimeResponseItem(
    @SerializedName("id") override val id: Int,

    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("name") val name: Name,
    @SerializedName("poster") val poster: Poster
): AnimeItemBase(id)