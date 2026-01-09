package com.example.network.releases.models.anime_details_item_response

import com.example.network.common.common_response_models.Genre
import com.example.network.common.common_response_models.Name
import com.example.network.common.common_response_models.Poster
import com.google.gson.annotations.SerializedName

data class AnimeDetailsItem(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String?,
    @SerializedName("episodes") val episodes: List<Episode>,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("is_ongoing") val isOngoing: Boolean,
    @SerializedName("members") val members: List<Member>,
    @SerializedName("name") val name: Name,
    @SerializedName("poster") val poster: Poster,
    @SerializedName("season") val season: Season,
    @SerializedName("torrents") val torrents: List<Torrent>,
    @SerializedName("type") val type: Type,
    @SerializedName("year") val year: Int,
)