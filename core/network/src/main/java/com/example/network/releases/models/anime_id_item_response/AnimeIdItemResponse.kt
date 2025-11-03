package com.example.network.releases.models.anime_id_item_response

import com.example.network.common.common_response_models.AnimeItemResponseBase
import com.google.gson.annotations.SerializedName

data class AnimeIdItemResponse(
    @SerializedName("id") override val id: Int
): AnimeItemResponseBase(id)