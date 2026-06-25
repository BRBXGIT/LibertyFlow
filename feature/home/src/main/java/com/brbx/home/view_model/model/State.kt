package com.brbx.home.view_model.model

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import arrow.optics.optics
import com.brbx.common.model.anime_item.model.AnimeItem
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Years
import com.brbx.common.model.loading_state.LoadingState
import com.brbx.common.model.search_state.SearchState
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting

@Immutable
@optics
internal data class State(
    val catalog: Catalog = Catalog(),
    val randomAnime: RandomAnime = RandomAnime(),
    val search: SearchState = SearchState(),
    val filtersSheet: FiltersSheet = FiltersSheet(),
) {
    @Immutable
    @optics
    data class Catalog(
        val loadingState: LoadingState = LoadingState(),
        val refreshingState: LoadingState = LoadingState(),
        val catalog: PagingData<AnimeItem> = PagingData.empty(),
    ) { companion object }

    @Immutable
    @optics
    data class RandomAnime(val loadingState: LoadingState = LoadingState()) {
        companion object
    }

    @Immutable
    @optics
    data class FiltersSheet(
        val isVisible: Boolean = false, // TODO Maybe make reusable
        val filers: Filers = Filers(),
    ) {
        @Immutable
        @optics
        data class Filers(
            val isOngoing: Boolean = false,
            val sorting: Sorting = Sorting.CreatedAtDesc,
            val years: Years = Years(),
            val seasons: List<Season> = emptyList(),
            val genres: List<Genre> = emptyList(),
        ) { companion object }

        companion object
    }

    companion object
}
