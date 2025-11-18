package com.example.network.common.common_pagination.anime_items_pagination

import com.example.network.common.common_response_models.AnimeResponseItem
import com.example.network.common.common_pagination.meta.Meta
import com.google.gson.annotations.SerializedName

data class AnimeItemsPagination(
    @SerializedName("data") val data: List<AnimeResponseItem>,
    @SerializedName("meta") val meta: Meta
)