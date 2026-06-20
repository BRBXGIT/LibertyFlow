package com.brbx.domain.user.lists.collections.collections_releases.repository

import androidx.paging.PagingData
import com.brbx.domain.model.common.Collection
import com.brbx.domain.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

interface UserCollectionsReleasesRepository {

    fun getReleases(collection: Collection): Flow<PagingData<DomainAnimeItem>>
}