package com.example.network.common.common_pagination.meta

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object containing detailed pagination information.
 *
 * @property count Number of items in the current response.
 * @property currentPage The index of the current page.
 * @property linksDto Navigation links for traversing pages.
 * @property perPage The number of items requested per page.
 * @property total The total number of items available across all pages.
 * @property totalPages The total number of pages available.
 */
data class PaginationDto(
    @field:SerializedName(FIELD_COUNT)
    val count: Int,

    @field:SerializedName(FIELD_CURRENT_PAGE)
    val currentPage: Int,

    @field:SerializedName(FIELD_LINKS)
    val linksDto: LinksDto,

    @field:SerializedName(FIELD_PER_PAGE)
    val perPage: Int,

    @field:SerializedName(FIELD_TOTAL)
    val total: Int,

    @field:SerializedName(FIELD_TOTAL_PAGES)
    val totalPages: Int
) {
    companion object Fields {
        private const val FIELD_COUNT = "count"
        private const val FIELD_CURRENT_PAGE = "current_page"
        private const val FIELD_LINKS = "links"
        private const val FIELD_PER_PAGE = "per_page"
        private const val FIELD_TOTAL = "total"
        private const val FIELD_TOTAL_PAGES = "total_pages"
    }
}