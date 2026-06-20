package com.brbx.network.base.model.response.common

import kotlinx.serialization.Serializable

@Serializable
sealed interface Genre {

    val name: String

    @JvmInline
    @Serializable
    value class Simple(override val name: String) : Genre

    @Serializable
    data class WithId(
        override val name: String,
        val id: Int,
    ) : Genre
}