package com.example.network.releases.models.anime_id_item_response

import com.example.network.common.common_models.AnimeItemResponseBase

data class AnimeIdItemResponse(
    override val id: Int
): AnimeItemResponseBase(id)