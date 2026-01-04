package com.example.common.reloads

import com.example.data.models.common.request.request_parameters.Collection

data class ReloadsState(
    val favoritesNeedReload: Boolean = false,
    val collectionNeedReload: Pair<Collection, Boolean> = Pair(Collection.WATCHING, false)
) {
    fun toggleFavoritesNeedReload() = copy(favoritesNeedReload = !favoritesNeedReload)

    fun updateCollectionNeedReload(collection: Collection, needReload: Boolean) = copy(collectionNeedReload = Pair(collection, needReload))
}
