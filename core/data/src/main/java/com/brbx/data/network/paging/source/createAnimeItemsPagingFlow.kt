package com.brbx.data.network.paging.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.brbx.data.network.paging.model.DomainPaginatedAnimeItems
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.model.result.DomainRequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

internal inline fun createAnimeItemsPagingFlow(
    limit: Int,
    invalidateTrigger: Flow<Any?>,
    crossinline call: suspend (page: Int) -> DomainRequestResult<DomainPaginatedAnimeItems>,
): Flow<PagingData<DomainAnimeItem>> {
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
    crossinline call: suspend (page: Int) -> DomainRequestResult<DomainPaginatedAnimeItems>,
): Flow<PagingData<DomainAnimeItem>> =
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