package com.brbx.home.view_model.model

import com.brbx.common.model.common.model.Genre
import com.brbx.common.view_model.model.intent.CommonSearchIntent
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting

internal sealed interface Intent {

    data object GetRandomAnime : Intent

    data object GetLatestWatchingAnime : Intent

    sealed interface Catalog : Intent {
        data object SetUpPaging : Catalog

        data class SetLoading(val loading: Boolean) : Catalog
        data class SetRefreshing(val refreshing: Boolean) : Catalog
        data class SetLoadingException(val exception: Boolean) : Catalog
        data class SetRefreshingException(val exception: Boolean) : Catalog
    }

    @JvmInline value class Search(val action: CommonSearchIntent) : Intent

    sealed interface Filters : Intent {
        @JvmInline value class UpdateSorting(val sorting: Sorting) : Filters
        data class UpdateYears(val from: Int, val to: Int) : Filters

        @JvmInline value class ToggleSeason(val season: Season) : Filters
        @JvmInline value class ToggleGenre(val genre: Genre) : Filters
        data object ToggleSheet : Filters
        data object ToggleOngoing : Filters
    }
}