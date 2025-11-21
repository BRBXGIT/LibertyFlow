package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.request.request_parameters.UiFullRequestParameters

@Immutable
data class HomeState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val randomAnimeId: Int? = null,
    val isSearching: Boolean = false,
    val isFiltersBSVisible: Boolean = false,

    val request: UiFullRequestParameters = UiFullRequestParameters(),

    val genres: List<UiGenre> = emptyList(),
    val isGenresLoading: Boolean = false
)
