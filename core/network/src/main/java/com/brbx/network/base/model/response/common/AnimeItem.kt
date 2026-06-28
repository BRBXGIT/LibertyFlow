package com.brbx.network.base.model.response.common

import com.brbx.network.base.model.common.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeItem(
    @SerialName("added_in_users_favorites") val addedInUsersFavorites: Int? = null,
    val genres: List<Genre> = emptyList(),
    val id: Int,
    val name: Name,
    val poster: Poster,
)