package com.example.common.refresh

import com.example.data.models.common.request.request_parameters.Collection

sealed interface RefreshEffect {
    data object RefreshFavorites: RefreshEffect
    data class RefreshCollection(val collection: Collection): RefreshEffect
}