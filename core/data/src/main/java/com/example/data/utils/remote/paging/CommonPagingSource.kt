package com.example.data.utils.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPagination
import com.example.network.common.common_request_models.common_request_base.CommonRequestBase
import com.example.network.common.common_response_models.AnimeResponseItem
import retrofit2.Response

internal class CommonPagingSource(
    private val apiCall: suspend (CommonRequestBase) -> Response<AnimeItemsPagination>,
    private val baseRequest: CommonRequestBase,
): PagingSource<Int, AnimeResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponseItem> {
        val key = params.key ?: 1
        val currentRequest = baseRequest.withPageAndLimit(key)

        var loadResult: LoadResult<Int, AnimeResponseItem>? = null

        NetworkRequest.safeApiCall(
            call = { apiCall(currentRequest) },
            map = { it }
        ).onSuccess {
            val pagination = it.meta.pagination
            loadResult = LoadResult.Page(
                data = it.data,
                prevKey = if (key > 1) key - 1 else null,
                nextKey = if (key < pagination.totalPages) key + 1 else null
            )
        }.onError { _, message ->
            loadResult = LoadResult.Error(Exception(message.toString()))
        }

        return loadResult!!
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeResponseItem>): Int? {
        return state.anchorPosition
    }
}