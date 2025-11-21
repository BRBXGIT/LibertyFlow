package com.example.network.collections.models

import com.google.gson.annotations.SerializedName

data class CollectionItem(
    @SerializedName("release_id") val releaseId: Int,
    @SerializedName("type_of_collection") val collectionType: String
)
