package com.brbx.data.user.lists.favorites.releases.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.brbx.data.common.map.toData
import com.brbx.data.common.map.toDomain
import com.brbx.data.paging.anime_item.createAnimeItemsPagingFlow
import com.brbx.data.user.lists.favorites.interactor.FavoritesIdsSource
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.user.lists.favorites.favorites_releases.repository.UserFavoritesReleasesRepository
import com.brbx.network.account_user.lists.favorites.favorites_releases.api.AccountUserFavoritesReleasesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserFavoritesReleasesRepositoryImpl(
    private val api: AccountUserFavoritesReleasesApi,
    private val source: FavoritesIdsSource,
) : UserFavoritesReleasesRepository {

    override fun getReleases(request: DomainRequest.Simple): Flow<PagingData<DomainAnimeItem>> {
        val dataRequest = request.toData()

        return createAnimeItemsPagingFlow(
            limit = dataRequest.limit,
            invalidateTrigger = source.ids,
        ) { page ->
            api.getFavorites(request = dataRequest.copy(page = page))
        }.map { pagingData -> pagingData.map { it.toDomain() } }
    }
}