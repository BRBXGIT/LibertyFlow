package com.brbx.domain.user.lists.collections.collections_releases.use_case

import androidx.paging.PagingData
import com.brbx.domain.model.common.Collection
import com.brbx.domain.model.request.DomainParameters
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.user.lists.collections.collections_releases.repository.UserCollectionsReleasesRepository
import kotlinx.coroutines.flow.Flow

class GetUserCollectionReleasesUseCase(
    private val repository: UserCollectionsReleasesRepository,
) {
    operator fun invoke(
        search: String,
        collection: Collection,
    ): Flow<PagingData<DomainAnimeItem>> {
        val request = DomainRequest.Collection(
            collection = collection,
            parameters = DomainParameters.Simple.WithoutSorting(search = search)
        )
        return repository.getReleases(request)
    }
}