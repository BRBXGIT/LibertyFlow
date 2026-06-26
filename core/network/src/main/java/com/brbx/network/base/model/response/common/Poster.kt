package com.brbx.network.base.model.response.common

import kotlinx.serialization.Serializable

@Serializable
data class Poster(
    val optimized: Optimized,
) {
    @Serializable
    data class Optimized(
        val preview: String,
        val src: String,
        val thumbnail: String,
    )
}