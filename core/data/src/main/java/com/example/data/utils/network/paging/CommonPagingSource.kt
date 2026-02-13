package com.example.data.utils.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.utils.network.network_request.NetworkRequest
import com.example.data.utils.network.network_request.NetworkResult
import com.example.network.common.common_pagination_models.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_request_models.common_request_base.CommonRequestDtoBase
import com.example.network.common.common_response_models.AnimeResponseItemDto
import retrofit2.Response

private const val ONE = 1

internal class CommonPagingSource(
    private val apiCall: suspend (CommonRequestDtoBase) -> Response<AnimeItemsPaginationDto>,
    private val baseRequest: CommonRequestDtoBase,
): PagingSource<Int, AnimeResponseItemDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponseItemDto> {
        val page = params.key ?: ONE
        val request = baseRequest.withPageAndLimit(page)

        return when (
            val result = NetworkRequest.safeApiCall(
                call = { apiCall(request) },
                map = { it }
            )
        ) {
            is NetworkResult.Success -> {
                val pagination = result.data.metaDto.paginationDto
                LoadResult.Page(
                    data = result.data.data,
                    prevKey = if (page > ONE) page - ONE else null,
                    nextKey = if (page < pagination.totalPages) page + ONE else null
                )
            }
            is NetworkResult.Error -> {
                LoadResult.Error(Exception(result.messageRes.toString()))
            }
        }
    }


    override fun getRefreshKey(state: PagingState<Int, AnimeResponseItemDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(ONE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(ONE)
        }
    }
}