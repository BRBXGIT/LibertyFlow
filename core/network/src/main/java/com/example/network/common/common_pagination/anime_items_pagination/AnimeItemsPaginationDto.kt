package com.example.network.common.common_pagination.anime_items_pagination

import com.example.network.common.common_response_models.AnimeResponseItemDto
import com.example.network.common.common_pagination.meta.MetaDto
import com.google.gson.annotations.SerializedName

data class AnimeItemsPaginationDto(
    @SerializedName("data") val data: List<AnimeResponseItemDto>,
    @SerializedName("meta") val metaDto: MetaDto
)