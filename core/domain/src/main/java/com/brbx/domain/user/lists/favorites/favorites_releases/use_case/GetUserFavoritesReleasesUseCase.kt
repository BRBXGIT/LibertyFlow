package com.brbx.domain.user.lists.favorites.favorites_releases.use_case

import androidx.paging.PagingData
import com.brbx.domain.model.response.common.DomainAnimeItem
import com.brbx.domain.user.lists.favorites.favorites_releases.repository.UserFavoritesReleasesRepository
import kotlinx.coroutines.flow.Flow

class GetUserFavoritesReleasesUseCase(
    private val repository: UserFavoritesReleasesRepository,
) {
    operator fun invoke(): Flow<PagingData<DomainAnimeItem>> =
        repository.getReleases()
}