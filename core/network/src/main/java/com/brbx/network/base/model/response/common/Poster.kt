package com.brbx.network.base.model.response.common

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Poster(
    val optimized: Optimized,
) {
    @Serializable
    data class Optimized(
        val preview: String,
        val src: String,
        val thumbnail: String,
    )
}