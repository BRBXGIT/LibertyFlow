package com.brbx.data.network.user.lists.collections.releases.repository

import androidx.paging.PagingData
import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.common.toData
import com.brbx.data.network.paging.map.toDomain
import com.brbx.data.network.paging.source.createAnimeItemsPagingFlow
import com.brbx.data.network.user.lists.collections.interactor.CollectionsIdsSource
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.user.lists.collections.collections_releases.repository.UserCollectionsReleasesRepository
import com.brbx.network.account_user.lists.collections.collections_releases.api.AccountUserCollectionsReleasesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class UserCollectionsReleasesRepositoryImpl(
    private val api: AccountUserCollectionsReleasesApi,
    private val source: CollectionsIdsSource,
    private val executor: ApiCallExecutor,
) : UserCollectionsReleasesRepository {

    override fun getReleases(
        request: DomainRequest.Collection
    ): Flow<PagingData<DomainAnimeItem>> {
        val dataRequest = request.toData()

        val collectionInvalidateTrigger = source.ids
            .map { idsMap -> idsMap?.get(request.collection) }
            .distinctUntilChanged()
        return createAnimeItemsPagingFlow(
            limit = dataRequest.limit,
            invalidateTrigger = collectionInvalidateTrigger,
        ) { page ->
            val withPage = dataRequest.copy(page = page)
            executor.execute(mapper = { it.toDomain() }) { api.getCollection(request = withPage) }
        }
    }
}