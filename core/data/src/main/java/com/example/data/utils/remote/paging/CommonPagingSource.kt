package com.example.data.utils.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.NetworkResult
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPagination
import com.example.network.common.common_request_models.common_request.CommonRequest
import com.example.network.common.common_request_models.common_request.CommonRequestBase
import com.example.network.common.common_request_models.common_request.CommonRequestWithCollectionType
import com.example.network.common.common_response_models.AnimeResponseItem
import com.example.network.common.common_utils.CommonNetworkUtils
import retrofit2.Response

internal class CommonPagingSource(
    private val apiCall: suspend (CommonRequestBase) -> Response<AnimeItemsPagination>,
    private val baseRequest: CommonRequestBase,
): PagingSource<Int, AnimeResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponseItem> {
        val page = params.key ?: CommonNetworkUtils.COMMON_START_PAGE
        val currentRequest = when(baseRequest) {
            is CommonRequest -> {
                baseRequest.copy(
                    page = page,
                    limit = params.loadSize
                )
            }
            is CommonRequestWithCollectionType -> {
                baseRequest.copy(
                    page = page,
                    limit = params.loadSize
                )
            }
            else -> baseRequest
        }

        var loadResult: LoadResult<Int, AnimeResponseItem>? = null

        NetworkRequest.safeApiCall(
            call = { apiCall(currentRequest) },
            map = { it }
        ).onSuccess {
            loadResult = LoadResult.Page(
                data = it.data,
                prevKey = it.meta.pagination.previousPage(),
                nextKey = it.meta.pagination.nextPage()
            )
        }.onError { message ->
            loadResult = LoadResult.Error(Exception(message))
        }

        return loadResult!!
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeResponseItem>): Int? {
        return state.anchorPosition
    }
}