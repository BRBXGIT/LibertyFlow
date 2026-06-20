package com.brbx.domain.model.response.common

@JvmInline
value class DomainPoster(
    val optimized: Optimized,
) {
    data class Optimized(
        val preview: String,
        val src: String,
        val thumbnail: String,
    )
}