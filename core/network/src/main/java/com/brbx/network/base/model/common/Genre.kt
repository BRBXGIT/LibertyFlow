package com.brbx.network.base.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val name: String,
    val id: Int,
)