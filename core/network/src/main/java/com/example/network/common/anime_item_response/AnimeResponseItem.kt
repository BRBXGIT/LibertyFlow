package com.example.network.common.anime_item_response

import com.example.network.common.AnimeItemResponseBase
import com.google.gson.annotations.SerializedName

data class AnimeResponseItem(
    @SerializedName("id") override val id: Int,

    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("name") val name: Name,
    @SerializedName("poster") val poster: Poster,
): AnimeItemResponseBase {
    fun genresAsString() = genres.joinToString(", ") { it.name }

    fun posterUrl(baseUrl: String) = baseUrl + poster.thumbnail
}