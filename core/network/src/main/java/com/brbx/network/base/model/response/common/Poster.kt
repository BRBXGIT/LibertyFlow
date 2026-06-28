package com.brbx.network.base.model.response.common

import kotlinx.serialization.Serializable

@Serializable
data class Poster(
    val optimized: Optimized = Optimized(),
) {
    @Serializable
    data class Optimized(
        val preview: String? = null,
        val src: String? = null,
        val thumbnail: String? = null,
    )
}