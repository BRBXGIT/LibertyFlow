package com.example.network.common.common_response_models

import com.example.network.common.common_models.AnimeItemResponseBase
import com.example.network.common.common_models.Genre
import com.example.network.common.common_models.Name
import com.example.network.common.common_models.Poster
import com.google.gson.annotations.SerializedName

data class AnimeResponseItem(
    @SerializedName("id") override val id: Int,

    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("name") val name: Name,
    @SerializedName("poster") val poster: Poster
): AnimeItemResponseBase(id)