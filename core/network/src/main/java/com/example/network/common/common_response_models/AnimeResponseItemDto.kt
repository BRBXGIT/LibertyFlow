package com.example.network.common.common_response_models

import com.google.gson.annotations.SerializedName

data class AnimeResponseItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("genres") val genres: List<GenreDto>,
    @SerializedName("name") val nameDto: NameDto,
    @SerializedName("poster") val posterDto: PosterDto
)