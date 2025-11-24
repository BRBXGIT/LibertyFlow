package com.example.collections.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.common.request.request_parameters.CollectionType

@Immutable
data class CollectionsState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,

    val query: String = "",

    val selectedCollection: CollectionType = CollectionType.WATCHING
)
