package com.example.network.releases.models.anime_details_item_response

import com.example.network.common.common_response_models.GenreDto
import com.example.network.common.common_response_models.NameDto
import com.example.network.common.common_response_models.PosterDto
import com.google.gson.annotations.SerializedName

/**
 * The primary Data Transfer Object for full anime details.
 * This model aggregates metadata, media links, cast members, and distribution data.
 */
data class AnimeDetailsItemDto(
    @field:SerializedName(FIELD_ID)
    val id: Int,

    @field:SerializedName(FIELD_DESCRIPTION)
    val description: String?,

    @field:SerializedName(FIELD_EPISODES)
    val episodes: List<EpisodeDto>,

    @field:SerializedName(FIELD_GENRES)
    val genres: List<GenreDto>,

    @field:SerializedName(FIELD_IS_ONGOING)
    val isOngoing: Boolean,

    @field:SerializedName(FIELD_MEMBERS)
    val members: List<MemberDto>,

    @field:SerializedName(FIELD_NAME)
    val nameDto: NameDto,

    @field:SerializedName(FIELD_POSTER)
    val posterDto: PosterDto,

    @field:SerializedName(FIELD_SEASON)
    val seasonDto: SeasonDto,

    @field:SerializedName(FIELD_TORRENTS)
    val torrents: List<TorrentDto>,

    @field:SerializedName(FIELD_TYPE)
    val typeDto: TypeDto,

    @field:SerializedName(FIELD_YEAR)
    val year: Int
) {
    companion object Fields {
        private const val FIELD_ID = "id"
        private const val FIELD_DESCRIPTION = "description"
        private const val FIELD_EPISODES = "episodes"
        private const val FIELD_GENRES = "genres"
        private const val FIELD_IS_ONGOING = "is_ongoing"
        private const val FIELD_MEMBERS = "members"
        private const val FIELD_NAME = "name"
        private const val FIELD_POSTER = "poster"
        private const val FIELD_SEASON = "season"
        private const val FIELD_TORRENTS = "torrents"
        private const val FIELD_TYPE = "type"
        private const val FIELD_YEAR = "year"
    }
}