package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.common.ui_anime_item.UiAnimeItem

@Immutable
data class HomeState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,

    val latestReleases: List<UiAnimeItem> = emptyList(),
    val randomAnimeId: Int? = null,

    val isSearching: Boolean = false,
    val query: String = ""
)