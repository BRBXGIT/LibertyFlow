package com.brbx.data.network.paging.model

import com.brbx.domain.network.model.response.common.DomainAnimeItem

internal data class DomainPaginatedAnimeItems(
    val items: List<DomainAnimeItem>,
    val pagination: DomainPagination,
) {
    data class DomainPagination(
        val currentPage: Int,
        val perPage: Int,
        val totalPages: Int,
        val total: Int,
        val count: Int,
    )
}