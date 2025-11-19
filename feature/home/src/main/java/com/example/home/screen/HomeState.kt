package com.example.home.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.common.ui_anime_item.UiAnimeItem

@Immutable
data class HomeState(
    // whether an error occurred while loading the current content
    val isError: Boolean = false,

    // whether refresh / paging is currently loading
    val isLoading: Boolean = false,

    // list of latest releases (non-paged)
    val latestReleases: List<UiAnimeItem> = emptyList(),

    // optional id from "get random anime"
    val randomAnimeId: Int? = null,

    // when true — search UI is visible / active
    val isSearching: Boolean = false,

    // current query text
    val query: String = ""
)
