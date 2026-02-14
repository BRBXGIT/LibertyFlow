package com.example.common.refresh

import com.example.data.models.common.request.request_parameters.Collection

/**
 * Defines the set of side effects related to data invalidation or UI refreshes.
 * * Use [RefreshFavorites] to trigger update of favorited items.
 * * Use [RefreshCollection] to trigger update for a specific collection.
 */
sealed interface RefreshEffect {
    data object RefreshFavorites: RefreshEffect

    data class RefreshCollection(val collection: Collection?): RefreshEffect
}