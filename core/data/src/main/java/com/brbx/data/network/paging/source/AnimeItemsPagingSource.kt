package com.brbx.data.network.paging.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.brbx.data.network.paging.map.toPagingException
import com.brbx.data.network.paging.model.DomainPaginatedAnimeItems
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.result.DomainRequestResult

internal class AnimeItemsPagingSource(
    private val call: suspend (page: Int) -> DomainRequestResult<DomainPaginatedAnimeItems>
) : PagingSource<Int, DomainAnimeItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DomainAnimeItem> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return when (val result = call(page)) {
            is DomainRequestResult.Success<DomainPaginatedAnimeItems> -> {
                val resultData = result.data
                val pagination = resultData.pagination

                LoadResult.Page(
                    data = resultData.items,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (page < pagination.totalPages) page + 1 else null
                )
            }
            is DomainRequestResult.Error -> {
                LoadResult.Error(throwable = result.exception.toPagingException())
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DomainAnimeItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(other = 1) ?: closestPage?.nextKey?.minus(other = 1)
        }
    }

    private companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}