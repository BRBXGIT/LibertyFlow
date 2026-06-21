package com.brbx.data.user.lists.favorites.interactor

import kotlinx.coroutines.flow.StateFlow

internal interface FavoritesIdsSource {

    val ids: StateFlow<List<Int>?>
}