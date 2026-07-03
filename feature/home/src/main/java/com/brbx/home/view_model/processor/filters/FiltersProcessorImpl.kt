package com.brbx.home.view_model.processor.filters

import arrow.optics.copy
import com.brbx.common.model.common.map.toAnimeCardModel
import com.brbx.common.model.common.model.Years
import com.brbx.common.view_model.processor.loading.model.isException
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.domain.network.genres.get.use_case.GetAnimeGenresUseCase
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
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

    override fun LibertyFlowMviScope<State>.process(intent: Intent.Filters) {
        when (intent) {
            Intent.Filters.ToggleSheet -> updateState {
                copy { State.filtersSheet.isVisible transform { !it } }
            }
            Intent.Filters.ToggleOngoing -> updateState {
                copy { State.filtersSheet.filters.isOngoing transform { !it } }
            }
            is Intent.Filters.UpdateYears -> updateState {
                copy { State.filtersSheet.filters.years set Years(intent.from, intent.to) }
            }
            is Intent.Filters.UpdateSorting -> updateState {
                copy { State.filtersSheet.filters.sorting set intent.sorting }
            }
            is Intent.Filters.ToggleGenre -> updateState {
                copy {
                    State.filtersSheet.filters.genresState.selectedGenres transform {
                        it.toggle(element = intent.genre)
                    }
                }
            }
            is Intent.Filters.ToggleSeason -> updateState {
                copy {
                    State.filtersSheet.filters.seasons transform {
                        it.toggle(element = intent.season)
                    }
                }
            }
            is Intent.Filters.LoadGenres -> {
                coroutineScope.launch(context = dispatcherIo) {
                    makeNetworkCall(
                        loadingLens = State.filtersSheet.filters.genresState.loading,
                        call = { genresUseCase() },
                    ).onSuccess { genres ->
                        val mapped = genres.map { genre -> genre.toAnimeCardModel() }
                        updateState {
                            copy { State.filtersSheet.filters.genresState.genres set mapped }
                        }
                    } onException {
                        updateState {
                            copy { State.filtersSheet.filters.genresState.loading.isException set true }
                        }
                    }
                }
            }
        }
    }

    private fun <T> List<T>.toggle(element: T): List<T> =
        if (contains(element)) this - element else this + element
}