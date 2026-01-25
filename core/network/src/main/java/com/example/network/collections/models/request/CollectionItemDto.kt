package com.example.network.collections.models.request

import com.google.gson.annotations.SerializedName

data class CollectionItemDto(
    @SerializedName("release_id") val releaseId: Int,
    @SerializedName("type_of_collection") val collectionType: String
)