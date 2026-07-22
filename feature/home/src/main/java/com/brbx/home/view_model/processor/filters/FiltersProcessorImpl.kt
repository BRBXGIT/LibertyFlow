package com.brbx.home.view_model.processor.filters

import arrow.optics.copy
import com.brbx.common.model.common.map.toUi
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Years
import com.brbx.common.utils.toggle
import com.brbx.common.view_model.processor.loading.model.isException
import com.brbx.common.view_model.view_model.processor.LibertyFlowIntentProcessor
import com.brbx.domain.network.genres.get.use_case.GetAnimeGenresUseCase
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.model.filters
import com.brbx.home.view_model.model.filtersSheet
import com.brbx.home.view_model.model.genres
import com.brbx.home.view_model.model.genresState
import com.brbx.home.view_model.model.isOngoing
import com.brbx.home.view_model.model.isVisible
import com.brbx.home.view_model.model.loading
import com.brbx.home.view_model.model.seasons
import com.brbx.home.view_model.model.selectedGenres
import com.brbx.home.view_model.model.sorting
import com.brbx.home.view_model.model.years
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class FiltersProcessorImpl(
    private val genresUseCase: GetAnimeGenresUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : LibertyFlowIntentProcessor<HomeState, HomeIntent.Filters>(),
    FiltersProcessor {

    override fun process(intent: HomeIntent.Filters) {
        when (intent) {
            HomeIntent.Filters.ToggleSheet -> toggleFiltersSheet()
            HomeIntent.Filters.ToggleOngoing -> toggleOngoing()
            is HomeIntent.Filters.UpdateYears -> updateYears(intent.from, intent.to)
            is HomeIntent.Filters.UpdateSorting -> updateSorting(intent.sorting)
            is HomeIntent.Filters.ToggleGenre -> toggleGenre(intent.genre)
            is HomeIntent.Filters.ToggleSeason -> toggleSeason(intent.season)
            HomeIntent.Filters.LoadGenres -> loadGenres()
        }
    }

    private fun toggleFiltersSheet() {
        updateState {
            copy {
                HomeState.filtersSheet.isVisible transform Boolean::not
            }
        }
    }

    private fun toggleOngoing() {
        updateState {
            copy {
                HomeState.filtersSheet.filters.isOngoing transform Boolean::not
            }
        }
    }

    private fun updateYears(from: Int, to: Int) {
        updateState {
            copy {
                HomeState.filtersSheet.filters.years set Years(from, to)
            }
        }
    }

    private fun updateSorting(sorting: Sorting) {
        updateState {
            copy {
                HomeState.filtersSheet.filters.sorting set sorting
            }
        }
    }

    private fun toggleGenre(genre: Genre) {
        updateState {
            copy {
                HomeState.filtersSheet.filters.genresState.selectedGenres transform {
                    it.toggle(element = genre)
                }
            }
        }
    }

    private fun toggleSeason(season: Season) {
        updateState {
            copy {
                HomeState.filtersSheet.filters.seasons transform {
                    it.toggle(season)
                }
            }
        }
    }

    private fun loadGenres() {
        coroutineScope.launch(dispatcherIo) {
            makeNetworkCall(
                loadingLens = HomeState.filtersSheet.filters.genresState.loading,
                call = genresUseCase::invoke,
            ).onSuccess { genres ->
                val mapped = genres.map { genre -> genre.toUi() }
                onGenresLoaded(genres = mapped)
            } onException {
                onGenresLoadingFailed()
            }
        }
    }

    private fun onGenresLoaded(genres: List<Genre>) {
        updateState {
            copy {
                HomeState.filtersSheet.filters.genresState.genres set genres.toSet()
            }
        }
    }

    private fun onGenresLoadingFailed() {
        updateState {
            copy {
                HomeState.filtersSheet.filters.genresState.loading.isException set true
            }
        }
    }
}