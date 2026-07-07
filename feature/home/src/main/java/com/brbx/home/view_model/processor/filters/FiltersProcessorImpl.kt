package com.brbx.home.view_model.processor.filters

import arrow.optics.copy
import com.brbx.common.model.common.map.toUi
import com.brbx.common.model.common.model.Years
import com.brbx.common.utils.toggle
import com.brbx.common.view_model.processor.loading.model.isException
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.domain.network.genres.get.use_case.GetAnimeGenresUseCase
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
) : FiltersProcessor {

    override fun LibertyFlowMviScope<HomeState>.process(intent: HomeIntent.Filters) {
        when (intent) {
            HomeIntent.Filters.ToggleSheet -> updateState {
                copy { HomeState.filtersSheet.isVisible transform { !it } }
            }
            HomeIntent.Filters.ToggleOngoing -> updateState {
                copy { HomeState.filtersSheet.filters.isOngoing transform { !it } }
            }
            is HomeIntent.Filters.UpdateYears -> updateState {
                copy { HomeState.filtersSheet.filters.years set Years(intent.from, intent.to) }
            }
            is HomeIntent.Filters.UpdateSorting -> updateState {
                copy { HomeState.filtersSheet.filters.sorting set intent.sorting }
            }
            is HomeIntent.Filters.ToggleGenre -> updateState {
                copy {
                    HomeState.filtersSheet.filters.genresState.selectedGenres transform {
                        it.toggle(element = intent.genre)
                    }
                }
            }
            is HomeIntent.Filters.ToggleSeason -> updateState {
                copy {
                    HomeState.filtersSheet.filters.seasons transform {
                        it.toggle(element = intent.season)
                    }
                }
            }
            is HomeIntent.Filters.LoadGenres -> {
                coroutineScope.launch(context = dispatcherIo) {
                    makeNetworkCall(
                        loadingLens = HomeState.filtersSheet.filters.genresState.loading,
                        call = { genresUseCase() },
                    ).onSuccess { genres ->
                        val mapped = genres.map { genre -> genre.toUi() }.toSet()
                        updateState {
                            copy { HomeState.filtersSheet.filters.genresState.genres set mapped }
                        }
                    } onException {
                        updateState {
                            copy { HomeState.filtersSheet.filters.genresState.loading.isException set true }
                        }
                    }
                }
            }
        }
    }
}