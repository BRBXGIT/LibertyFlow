package com.example.common.reloads

import com.example.data.models.common.request.request_parameters.Collection

sealed interface ReloadsIntent {
    data object ToggleFavoritesNeedReload: ReloadsIntent
    data class UpdateCollectionNeedReload(
        val collection: Collection,
        val needReload: Boolean
    ): ReloadsIntent
}