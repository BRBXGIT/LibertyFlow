package com.brbx.network.base.model.response.common

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val alternative: String?,
    val english: String,
    val main: String,
)