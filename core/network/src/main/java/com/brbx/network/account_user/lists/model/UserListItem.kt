package com.brbx.network.account_user.lists.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface UserListItem {
    val id: Int

    @Serializable
    data class Favorite(@SerialName("release_id") override val id: Int) : UserListItem

    @Serializable
    data class Collection(
        @SerialName("release_id") override val id: Int,
        @SerialName("type_of_collection") val collection: String,
    ) : UserListItem
}