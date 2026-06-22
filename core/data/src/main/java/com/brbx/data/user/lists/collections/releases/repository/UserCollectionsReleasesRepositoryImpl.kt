package com.brbx.data.user.lists.collections.releases.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.brbx.data.common.map.toData
import com.brbx.data.common.map.toDomain
import com.brbx.data.paging.anime_item.createAnimeItemsPagingFlow
import com.brbx.data.user.lists.collections.interactor.CollectionsIdsSource
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.user.lists.collections.collections_releases.repository.UserCollectionsReleasesRepository
import com.brbx.network.account_user.lists.collections.collections_releases.api.AccountUserCollectionsReleasesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class UserCollectionsReleasesRepositoryImpl(
    private val api: AccountUserCollectionsReleasesApi,
    private val source: CollectionsIdsSource,
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
            api.getCollection(request = withPage)
        }.map { pagingData -> pagingData.map { it.toDomain() } }
    }
}