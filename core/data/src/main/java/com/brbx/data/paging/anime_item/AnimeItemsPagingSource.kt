package com.brbx.data.paging.anime_item

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.brbx.network.base.model.response.common.AnimeItem
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult

internal class AnimeItemsPagingSource(
    private val call: suspend (page: Int) -> RequestResult<PaginatedAnimeItems>
) : PagingSource<Int, AnimeItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeItem> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return when (val result = call(page)) {
            is RequestResult.Success<PaginatedAnimeItems> -> {
                val resultData = result.data
                val pagination = resultData.meta.pagination

                LoadResult.Page(
                    data = resultData.items,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (page < pagination.totalPages) page + 1 else null
                )
            }
            is RequestResult.Error -> {
                LoadResult.Error(throwable = result.exception.toPagingException())
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(other = 1) ?: closestPage?.nextKey?.minus(other = 1)
        }
    }

    private companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}