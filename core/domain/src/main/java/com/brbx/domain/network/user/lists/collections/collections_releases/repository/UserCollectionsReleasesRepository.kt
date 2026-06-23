package com.brbx.domain.network.user.lists.collections.collections_releases.repository

import androidx.paging.PagingData
import com.brbx.domain.network.model.request.DomainRequest
import com.brbx.domain.network.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

interface UserCollectionsReleasesRepository {

    fun getReleases(request: DomainRequest.Collection): Flow<PagingData<DomainAnimeItem>>
}