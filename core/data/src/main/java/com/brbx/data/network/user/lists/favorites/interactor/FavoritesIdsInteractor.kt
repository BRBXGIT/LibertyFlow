package com.brbx.data.network.user.lists.favorites.interactor

import com.brbx.domain.network.user.lists.favorites.model.DomainFavoritesIds

internal interface FavoritesIdsInteractor {

    fun update(ids: DomainFavoritesIds?)
}