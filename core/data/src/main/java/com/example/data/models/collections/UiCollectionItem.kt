package com.example.data.models.collections

import com.example.data.models.common.request.request_parameters.CollectionType
import com.example.data.models.common.ui_anime_base.UiAnimeBase

data class UiCollectionItem(
    override val id: Int,

    val collectionType: CollectionType
): UiAnimeBase
