package com.example.network.common.common_pagination.anime_items_pagination

import com.example.network.common.common_response_models.AnimeResponseItemDto
import com.example.network.common.common_pagination.meta.MetaDto
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a paginated response of anime items.
 *
 * @property data The list of anime releases for the requested page.
 * @property metaDto Pagination metadata (e.g., total items, current page, limits).
 */
data class AnimeItemsPaginationDto(
    @field:SerializedName(FIELD_DATA)
    val data: List<AnimeResponseItemDto>,

    @field:SerializedName(FIELD_META)
    val metaDto: MetaDto
) {
    companion object Fields {
        private const val FIELD_DATA = "data"
        private const val FIELD_META = "meta"
    }
}