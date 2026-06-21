package com.brbx.domain.user.lists.favorites.favorites_releases.use_case

import androidx.paging.PagingData
import com.brbx.domain.model.request.DomainParameters
import com.brbx.domain.model.request.DomainRequest
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.user.lists.favorites.favorites_releases.repository.UserFavoritesReleasesRepository
import kotlinx.coroutines.flow.Flow

class GetUserFavoritesReleasesUseCase(
    private val repository: UserFavoritesReleasesRepository,
) {
    operator fun invoke(search: String): Flow<PagingData<DomainAnimeItem>> {
        val request = DomainRequest.Simple(
            parameters = DomainParameters.Simple.Default(search = search)
        )
        return repository.getReleases(request)
    }
}