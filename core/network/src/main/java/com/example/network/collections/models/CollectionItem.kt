package com.example.network.collections.models

import com.example.network.common.anime_base.AnimeBase
import com.google.gson.annotations.SerializedName

data class CollectionItem(
    @SerializedName("release_id") override val id: Int,
    @SerializedName("type_of_collection") val collectionType: String
): AnimeBase
