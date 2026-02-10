package com.example.data.utils.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.utils.remote.network_request.NetworkRequest
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.network.common.common_pagination.anime_items_pagination.AnimeItemsPaginationDto
import com.example.network.common.common_request_models.common_request_base.CommonRequestDtoBase
import com.example.network.common.common_response_models.AnimeResponseItemDto
import retrofit2.Response

internal class CommonPagingSource(
    private val apiCall: suspend (CommonRequestDtoBase) -> Response<AnimeItemsPaginationDto>,
    private val baseRequest: CommonRequestDtoBase,
): PagingSource<Int, AnimeResponseItemDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeResponseItemDto> {
        val key = params.key ?: 1
        val currentRequest = baseRequest.withPageAndLimit(key)

        var loadResult: LoadResult<Int, AnimeResponseItemDto>? = null

        NetworkRequest.safeApiCall(
            call = { apiCall(currentRequest) },
            map = { it }
        ).onSuccess {
            val pagination = it.metaDto.paginationDto
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

    override fun getRefreshKey(state: PagingState<Int, AnimeResponseItemDto>): Int? {
        return state.anchorPosition
    }
}