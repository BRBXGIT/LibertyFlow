package com.brbx.data.paging.anime_item

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.brbx.network.base.model.response.common.AnimeItem
import com.brbx.network.base.model.response.paginated.PaginatedAnimeItems
import com.brbx.network.base.model.result.RequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

internal inline fun createAnimeItemsPagingFlow(
    limit: Int,
    invalidateTrigger: Flow<Any?>,
    crossinline call: suspend (page: Int) -> RequestResult<PaginatedAnimeItems>,
): Flow<PagingData<AnimeItem>> {
    var currentSource: AnimeItemsPagingSource? = null

    val pagingFlow = Pager(
        config = PagingConfig(
            pageSize = limit,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            AnimeItemsPagingSource(
                call = { page -> call(page) }
            ).also { pagingSource -> currentSource = pagingSource }
        }
    ).flow

    return channelFlow {
        launch {
            invalidateTrigger
                .drop(count = 1)
                .collect {
                    currentSource?.invalidate()
                }
        }
        pagingFlow.collect { pagingData ->
            send(element = pagingData)
        }
    }
}

internal inline fun createAnimeItemsPagingFlow(
    limit: Int,
    crossinline call: suspend (page: Int) -> RequestResult<PaginatedAnimeItems>,
): Flow<PagingData<AnimeItem>> =
    Pager(
        config = PagingConfig(
            pageSize = limit,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            AnimeItemsPagingSource(
                call = { page -> call(page) }
            )
        }
    ).flow