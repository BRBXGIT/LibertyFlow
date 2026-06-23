package com.brbx.domain.network.user.lists.favorites.favorites_releases.repository

import androidx.paging.PagingData
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

interface UserFavoritesReleasesRepository {

    fun getReleases(request: DomainRequest.Simple): Flow<PagingData<DomainAnimeItem>>
}