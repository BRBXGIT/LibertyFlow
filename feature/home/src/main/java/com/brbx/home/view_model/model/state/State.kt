package com.brbx.home.view_model.model.state

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Years
import com.brbx.common.view_model.model.state.CommonLoadingState
import com.brbx.common.view_model.model.state.CommonPagingState
import com.brbx.common.view_model.model.state.CommonSearchState
import com.brbx.design_system.component.anime_card.AnimeCardModel
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting

@Immutable
@optics
internal data class State(
    val catalog: CommonPagingState<AnimeCardModel> = CommonPagingState(),
    val randomAnime: CommonLoadingState = CommonLoadingState(),
    val search: CommonSearchState = CommonSearchState(),
    val filtersSheet: FiltersSheet = FiltersSheet(),
    val tile: Tile? = null,
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
            val seasons: List<Season> = emptyList(),
            val genresState: Genres = Genres(),
        ) {
            @Immutable
            @optics
            data class Genres(
                val genres: List<Genre> = emptyList(),
                val loading: CommonLoadingState = CommonLoadingState(),
                val selectedGenres: List<Genre> = emptyList(),
            ) { companion object }

            companion object
        }

        companion object
    }

    companion object
}
