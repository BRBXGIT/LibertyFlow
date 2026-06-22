package com.brbx.domain.user.lists.collections.collections_releases.repository

import androidx.paging.PagingData
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

interface UserCollectionsReleasesRepository {

    fun getReleases(request: DomainRequest.Collection): Flow<PagingData<DomainAnimeItem>>
}