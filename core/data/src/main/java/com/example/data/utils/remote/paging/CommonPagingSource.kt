package com.example.data.utils.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPagination
import com.example.network.common.common_request_models.common_request.CommonRequest
import com.example.network.common.common_response_models.AnimeResponseItem
import retrofit2.Response

class CommonPagingSource(
    private val apiCall: suspend (CommonRequest) -> Response<AnimeItemsPagination>,
    private val baseRequest: CommonRequest,
): PagingSource<Int, AnimeResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponseItem> {
        val page = params.key ?: 1
        val currentRequest = baseRequest.copy(
            page = page,
            limit = params.loadSize
        )

        val result = NetworkRequest.safeApiCall(
            call = { apiCall(currentRequest) },
            map = { it }
        )

        return when(result) {
            is NetworkResult.Success -> {
                LoadResult.Page(
                    data = result.data.data,
                    prevKey = result.data.meta.pagination.previousPage(),
                    nextKey = result.data.meta.pagination.nextPage()
                )
            }
            is NetworkResult.Error -> LoadResult.Error(Exception(result.message))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeResponseItem>): Int? {
        return state.anchorPosition
    }
}