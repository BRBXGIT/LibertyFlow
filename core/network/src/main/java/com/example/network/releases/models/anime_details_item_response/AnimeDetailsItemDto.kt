package com.example.network.releases.models.anime_details_item_response

import com.example.network.common.common_response_models.GenreDto
import com.example.network.common.common_response_models.NameDto
import com.example.network.common.common_response_models.PosterDto
import com.google.gson.annotations.SerializedName

data class AnimeDetailsItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String?,
    @SerializedName("episodes") val episodes: List<EpisodeDto>,
    @SerializedName("genres") val genres: List<GenreDto>,
    @SerializedName("is_ongoing") val isOngoing: Boolean,
    @SerializedName("members") val members: List<MemberDto>,
    @SerializedName("name") val nameDto: NameDto,
    @SerializedName("poster") val posterDto: PosterDto,
    @SerializedName("season") val seasonDto: SeasonDto,
    @SerializedName("torrents") val torrents: List<TorrentDto>,
    @SerializedName("type") val typeDto: TypeDto,
    @SerializedName("year") val year: Int,
)