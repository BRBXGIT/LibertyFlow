package com.example.network.releases.models.anime_id_item_response

import com.example.network.common.anime_item_base.AnimeItemBase
import com.google.gson.annotations.SerializedName

data class AnimeIdItem(
    @SerializedName("id") override val id: Int
): AnimeItemBase(id)