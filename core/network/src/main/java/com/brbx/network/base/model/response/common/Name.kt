package com.brbx.network.base.model.response.common

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val alternative: String? = null,
    val english: String? = null,
    val main: String,
)