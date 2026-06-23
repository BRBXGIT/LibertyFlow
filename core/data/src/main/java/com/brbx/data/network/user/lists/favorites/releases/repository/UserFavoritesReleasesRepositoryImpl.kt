package com.brbx.data.network.user.lists.favorites.releases.repository

import androidx.paging.PagingData
import com.brbx.data.network.api.executor.ApiCallExecutor
import com.brbx.data.network.common.toData
import com.brbx.data.network.paging.map.toDomain
import com.brbx.data.network.paging.source.createAnimeItemsPagingFlow
import com.brbx.data.network.user.lists.favorites.interactor.FavoritesIdsSource
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import com.brbx.domain.network.user.lists.favorites.favorites_releases.repository.UserFavoritesReleasesRepository
import com.brbx.network.account_user.lists.favorites.favorites_releases.api.AccountUserFavoritesReleasesApi
import kotlinx.coroutines.flow.Flow

internal class UserFavoritesReleasesRepositoryImpl(
    private val api: AccountUserFavoritesReleasesApi,
    private val executor: ApiCallExecutor,
    private val source: FavoritesIdsSource,
) : UserFavoritesReleasesRepository {

    override fun getReleases(request: DomainRequest.Simple): Flow<PagingData<DomainAnimeItem>> {
        val dataRequest = request.toData()

        return createAnimeItemsPagingFlow(
            limit = dataRequest.limit,
            invalidateTrigger = source.ids,
        ) { page ->
            executor.execute(
                mapper = { it.toDomain() }
            ) {
                api.getFavorites(request = dataRequest.copy(page = page))
            }
        }
    }
}