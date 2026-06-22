package com.brbx.data.user.lists.favorites.interactor

import com.brbx.domain.user.lists.favorites.model.DomainFavoritesIds
import kotlinx.coroutines.flow.StateFlow

internal interface FavoritesIdsSource {

    val ids: StateFlow<DomainFavoritesIds?>
}