package com.brbx.data.user.lists.favorites.interactor

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class FavoritesIdsInteractorImpl : FavoritesIdsInteractor, FavoritesIdsSource {

    private val _ids = MutableStateFlow<List<Int>?>(value = null)
    override val ids = _ids.asStateFlow()

    override fun update(ids: List<Int>?) {
        _ids.update { ids }
    }
}