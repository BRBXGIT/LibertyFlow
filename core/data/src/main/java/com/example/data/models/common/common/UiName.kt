package com.example.data.models.common.common

import androidx.compose.runtime.Immutable

@Immutable
data class UiName(
    val russian: String,
    val english: String,
    val alternative: String?
)