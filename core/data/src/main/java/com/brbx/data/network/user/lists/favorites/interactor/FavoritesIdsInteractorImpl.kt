package com.brbx.data.network.user.lists.favorites.interactor

import com.brbx.domain.network.user.lists.favorites.model.DomainFavoritesIds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class FavoritesIdsInteractorImpl : FavoritesIdsInteractor, FavoritesIdsSource {

    private val _ids = MutableStateFlow<DomainFavoritesIds?>(value = null)
    override val ids = _ids.asStateFlow()

    override fun update(ids: DomainFavoritesIds?) {
        _ids.update { ids }
    }
}