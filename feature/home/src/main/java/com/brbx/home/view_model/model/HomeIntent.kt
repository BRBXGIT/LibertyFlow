package com.brbx.home.view_model.model

import com.brbx.common.model.common.model.Genre
import com.brbx.common.view_model.processor.paging.model.CommonPagingIntent
import com.brbx.common.view_model.processor.search.model.CommonSearchIntent
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.tile.model.CommonTileIntent
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting

internal sealed interface HomeIntent {

    data object GetRandomAnime : HomeIntent

    @JvmInline value class Tile(val action: CommonTileIntent) : HomeIntent

    @JvmInline value class Catalog(val action: CommonPagingIntent) : HomeIntent

    @JvmInline value class Search(val action: CommonSearchIntent) : HomeIntent

    @JvmInline value class Selection(val action: CommonSelectionIntent) : HomeIntent

    sealed interface Filters : HomeIntent {
        @JvmInline value class UpdateSorting(val sorting: Sorting) : Filters
        data class UpdateYears(val from: Int, val to: Int) : Filters

        @JvmInline value class ToggleSeason(val season: Season) : Filters
        @JvmInline value class ToggleGenre(val genre: Genre) : Filters
        data object ToggleSheet : Filters
        data object ToggleOngoing : Filters
        data object LoadGenres : Filters
    }
}