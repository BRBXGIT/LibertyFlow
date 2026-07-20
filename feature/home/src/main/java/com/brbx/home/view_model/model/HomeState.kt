package com.brbx.home.view_model.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.model.common.model.DefaultTile
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Years
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetState
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.common.view_model.processor.paging.model.CommonPagingState
import com.brbx.common.view_model.processor.search.model.CommonSearchState
import com.brbx.common.view_model.processor.selection.model.CommonSelectionState
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting

@Immutable
@optics
internal data class HomeState(
    val catalog: CommonPagingState<AnimeItem> = CommonPagingState(),
    val randomAnime: CommonLoadingState = CommonLoadingState(),
    val search: CommonSearchState = CommonSearchState(),
    val authSheetState: CommonAuthSheetState = CommonAuthSheetState(),
    val selection: CommonSelectionState = CommonSelectionState(),
    val filtersSheet: FiltersSheet = FiltersSheet(),
    val tile: DefaultTile? = null,
) {
    @Immutable
    @optics
    data class FiltersSheet(
        val isVisible: Boolean = false,
        val filters: Filters = Filters(),
    ) {
        @Immutable
        @optics
        data class Filters(
            val isOngoing: Boolean = false,
            val sorting: Sorting = Sorting.CreatedAtDesc,
            val years: Years = Years(),
            val seasons: Set<Season> = emptySet(),
            val genresState: Genres = Genres(),
        ) {
            @Immutable
            @optics
            data class Genres(
                val genres: Set<Genre> = emptySet(),
                val loading: CommonLoadingState = CommonLoadingState(),
                val selectedGenres: Set<Genre> = emptySet(),
            ) { companion object }

            companion object
        }

        companion object
    }

    companion object
}