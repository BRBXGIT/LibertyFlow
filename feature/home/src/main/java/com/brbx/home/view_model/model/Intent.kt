package com.brbx.home.view_model.model

import com.brbx.common.model.common.model.Genre
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting

internal sealed interface Intent {

    sealed interface Searching : Intent {
        data object ToggleSearching : Searching

        @JvmInline
        value class UpdateSearch(val search: String) : Searching
    }

    data object GetRandomAnime : Intent

    sealed interface Filters : Intent {
        data object ToggleSheet : Filters

        data object ToggleOngoing : Filters

        @JvmInline
        value class UpdateSorting(val sorting: Sorting) : Filters

        data class UpdateYears(val from: Int, val to: Int) : Filters

        @JvmInline
        value class ToggleSeason(val season: Season) : Filters

        @JvmInline
        value class ToggleGenre(val genre: Genre) : Filters
    }
}