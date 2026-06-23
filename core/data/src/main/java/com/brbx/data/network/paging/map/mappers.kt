package com.brbx.data.network.paging.map

import com.brbx.data.network.common.toDomain
import com.brbx.data.network.paging.model.DomainPaginatedAnimeItems
import com.brbx.data.network.paging.model.PagingException
import com.brbx.domain.network.model.result.RequestException
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems

internal fun RequestException.toPagingException(): PagingException =
    when (this) {
        RequestException.Conflict -> PagingException.Conflict()
        RequestException.TooManyRequests -> PagingException.TooManyRequests()
        RequestException.PayloadTooLarge -> PagingException.PayloadTooLarge()
        RequestException.ServerError -> PagingException.ServerError()
        RequestException.IncorrectCredentials -> PagingException.IncorrectCredentials()
        RequestException.Unauthorized -> PagingException.Unauthorized()
        RequestException.NoEmailOrPassword -> PagingException.NoEmailOrPassword()
        RequestException.RequestTimeout -> PagingException.RequestTimeout()
        RequestException.Internet -> PagingException.Internet()
        RequestException.Unknown -> PagingException.Unknown()
    }

internal fun PaginatedAnimeItems.toDomain(): DomainPaginatedAnimeItems =
    DomainPaginatedAnimeItems(
        items = this.items.map { it.toDomain() },
        pagination = this.meta.pagination.toDomain(),
    )

private fun PaginatedAnimeItems.Meta.Pagination.toDomain(): DomainPaginatedAnimeItems.DomainPagination =
    DomainPaginatedAnimeItems.DomainPagination(
        currentPage = this.currentPage,
        perPage = this.perPage,
        totalPages = this.totalPages,
        total = this.total,
        count = this.count,
    )