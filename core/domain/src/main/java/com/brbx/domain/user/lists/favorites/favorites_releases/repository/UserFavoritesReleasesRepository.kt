package com.brbx.domain.user.lists.favorites.favorites_releases.repository

import androidx.paging.PagingData
import com.brbx.domain.model.response.common.DomainAnimeItem
import kotlinx.coroutines.flow.Flow

interface UserFavoritesReleasesRepository {

    fun getReleases(): Flow<PagingData<DomainAnimeItem>>
}