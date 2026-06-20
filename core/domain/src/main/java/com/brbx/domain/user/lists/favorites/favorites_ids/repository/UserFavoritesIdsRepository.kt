package com.brbx.domain.user.lists.favorites.favorites_ids.repository

import com.brbx.domain.model.result.DomainRequestResult
import kotlinx.coroutines.flow.Flow

interface UserFavoritesIdsRepository {

    fun getIds(): Flow<DomainRequestResult<List<Int>>>
}