package com.example.data.models.collections.collection

import androidx.compose.runtime.Immutable
import com.example.data.models.common.request.request_parameters.Collection

@Immutable
data class AnimeCollection(
    val collection: Collection,
    val ids: List<Int>
)
