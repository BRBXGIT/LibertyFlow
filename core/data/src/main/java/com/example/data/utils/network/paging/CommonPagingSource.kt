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

/**
 * A generic [PagingSource] implementation for paginated anime data.
 * * This class handles the offset-based loading logic, interacting with [NetworkRequest]
 * to fetch and map data while managing prevKey and nextKey for the Paging library.
 *
 * @property apiCall A suspend function that takes a request DTO and returns a Retrofit [Response].
 * @property baseRequest The initial request configuration (filters, queries) to be paginated.
 */
internal class CommonPagingSource(
    private val apiCall: suspend (CommonRequestDtoBase) -> Response<AnimeItemsPaginationDto>,
    private val baseRequest: CommonRequestDtoBase,
) : PagingSource<Int, AnimeResponseItemDto>() {

    /**
     * Triggers a synchronous-like load of a specific page based on [params].
     * * Converts [NetworkResult.Success] into a [LoadResult.Page].
     * * Converts [NetworkResult.Error] into a [LoadResult.Error] by wrapping
     * the message resource ID in an Exception.
     */
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
                // Note: Paging 3 expects a Throwable for LoadResult.Error.
                // We wrap the message resource ID for the UI layer to resolve later.
                LoadResult.Error(Exception(result.messageRes.toString()))
            }
        }
    }

    /**
     * Provides the key (page number) to use when the data is refreshed or invalidated.
     * * Uses the state.anchorPosition to find the most relevant page currently in view.
     */
    override fun getRefreshKey(state: PagingState<Int, AnimeResponseItemDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(ONE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(ONE)
        }
    }
}