package com.brbx.home.view_model.model

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import arrow.optics.optics
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Years
import com.brbx.common.model.state.LoadingState
import com.brbx.common.model.state.SearchState
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Immutable
@optics
internal data class State(
    val catalog: Catalog = Catalog(),
    val randomAnime: RandomAnime = RandomAnime(),
    val search: SearchState = SearchState(),
    val filtersSheet: FiltersSheet = FiltersSheet(),
    val latestWatchingAnime: LatestWatchingAnime? = null,
) {
    @Immutable
    @optics
    data class Catalog(
        val loading: LoadingState = LoadingState(),
        val refreshing: LoadingState = LoadingState(),
        val catalog: Flow<PagingData<AnimeItem>> = emptyFlow(),
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

    @Immutable
    @optics
    data class LatestWatchingAnime(
        val animeId: Int,
        val title: String,
        val lastEpisode: Int,
    ) { companion object }

    companion object
}
