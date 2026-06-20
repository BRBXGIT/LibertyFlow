package com.brbx.domain.user.lists.collections.collections_releases.use_case

import androidx.paging.PagingData
import com.brbx.domain.model.common.Collection
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.user.lists.collections.collections_releases.repository.UserCollectionsReleasesRepository
import kotlinx.coroutines.flow.Flow

class GetUserCollectionReleasesUseCase(
    private val repository: UserCollectionsReleasesRepository,
) {
    operator fun invoke(collection: Collection): Flow<PagingData<DomainAnimeItem>> =
        repository.getReleases(collection)
}