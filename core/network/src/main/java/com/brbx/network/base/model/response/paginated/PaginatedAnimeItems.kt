package com.brbx.network.base.model.response.paginated

import com.brbx.network.base.model.response.common.AnimeItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedAnimeItems(
    @SerialName("data") val items: List<AnimeItem>,
    val meta: Meta,
) {
    @JvmInline
    @Serializable
    value class Meta(
        val pagination: Pagination,
    ) {
        @Serializable
        data class Pagination(
            @SerialName("current_page") val currentPage: Int,
            @SerialName("per_page") val perPage: Int,
            @SerialName("total_pages") val totalPages: Int,
            val total: Int,
            val links: Links,
            val count: Int,
        ) {
            @JvmInline
            @Serializable
            value class Links(val next: String)
        }
    }
}