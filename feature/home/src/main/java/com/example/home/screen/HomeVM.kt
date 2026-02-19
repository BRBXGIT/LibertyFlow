@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.filters.component.FiltersComponent
import com.example.common.vm_helpers.utils.toLazily
import com.example.data.domain.CatalogRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.utils.network.network_caller.onError
import com.example.data.utils.network.network_caller.onSuccess
import com.example.design_system.utils.CommonAnimationDelays.RAINBOW_BUTTON_ANIMATION_DELAY
import com.example.design_system.utils.CommonStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen, responsible for orchestrating anime discovery,
 * catalog filtering, and global UI effects.
 *
 * It utilizes [FiltersComponent] via class delegation to manage the complex state
 * of search filters seamlessly.
 *
 * @property filtersComponent Delegate handling the logic for search parameters and filter UI state.
 * @property releasesRepo Repository for fetching general anime releases and random suggestions.
 * @property catalogRepo Repository providing the paginated stream of filtered anime.
 * @property genresRepo Repository for fetching available anime genres.
 * @property dispatcherIo Optimized dispatcher for background network/disk operations.
 */
@HiltViewModel
class HomeVM @Inject constructor(
    private val filtersComponent: FiltersComponent,
    private val releasesRepo: ReleasesRepo,
    private val catalogRepo: CatalogRepo,
    private val genresRepo: GenresRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
): ViewModel(), FiltersComponent by filtersComponent {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.toLazily(HomeState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeFilters(
            scope = viewModelScope,
            onUpdate = { state -> _state.update { it.copy(filtersState = state) } }
        )
    }

    // --- Intents ---
    fun sendIntent(intent: HomeIntent) {
        when (intent) {
            // UI Visibility state
            HomeIntent.ToggleSearching -> toggleIsSearching()
            HomeIntent.ToggleFiltersBottomSheet -> toggleFiltersBS()

            // Filter mutations
            is HomeIntent.UpdateQuery -> updateQuery(intent.query)
            is HomeIntent.UpdateSorting -> updateSorting(intent.sorting)
            is HomeIntent.ToggleIsOngoing -> toggleIsOngoing()

            // Genre management
            is HomeIntent.AddGenre -> addGenre(intent.genre)
            is HomeIntent.RemoveGenre -> removeGenre(intent.genre)

            // --- Season ---
            is HomeIntent.AddSeason -> addSeason(intent.season)
            is HomeIntent.RemoveSeason -> removeSeason(intent.season)

            // --- Years ---
            is HomeIntent.UpdateFromYear -> updateFromYear(intent.year)
            is HomeIntent.UpdateToYear -> updateToYear(intent.year)

            // Global Loading/Error states
            is HomeIntent.SetLoading ->
                _state.update { it.copy(loadingState = it.loadingState.withLoading(intent.value)) }
            is HomeIntent.SetError ->
                _state.update { it.copy(loadingState = it.loadingState.withError(intent.value)) }

            // Data Fetching
            HomeIntent.GetRandomAnime -> getRandomAnime()
            HomeIntent.GetGenres -> getGenres()
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    // --- Data streams ---
    val anime = _state
        .map { it.filtersState.requestParameters }
        .distinctUntilChanged()
        .flatMapLatest { request ->
            catalogRepo.getAnimeByQuery(CommonRequest(request))
        }
        .cachedIn(viewModelScope)

    // --- Private Business Logic ---
    private fun getRandomAnime() {
        viewModelScope.launch(dispatcherIo) {
            // Reset state and show loading
            _state.update { it.copy(randomAnimeState = it.randomAnimeState.withBoth(loading = true, error = false)) }

            delay(RAINBOW_BUTTON_ANIMATION_DELAY) // Artificial delay for animation polish

            releasesRepo.getRandomAnime()
                .onSuccess { anime ->
                    sendEffect(UiEffect.Navigate(AnimeDetailsRoute(anime.id)))
                }
                .onError { _, messageRes ->
                    _state.update { it.copy(randomAnimeState = it.randomAnimeState.withError(true)) }
                    sendSnackbarWithAction(messageRes) { getRandomAnime() }
                }

            _state.update { it.copy(randomAnimeState = it.randomAnimeState.withLoading(false)) }
        }
    }

    private fun getGenres() {
        viewModelScope.launch(dispatcherIo) {
            _state.update {
                it.copy(
                    genresState = it.genresState.copy(
                        loadingState = it.genresState.loadingState.withLoading(true)
                    )
                )
            }

            genresRepo.getGenres()
                .onSuccess { genres -> _state.update { it.copy(genresState = it.genresState.withGenres(genres)) } }
                .onError { _, messageRes -> sendSnackbarWithAction(messageRes) { getGenres() } }

            _state.update {
                it.copy(
                    genresState = it.genresState.copy(
                        loadingState = it.genresState.loadingState.withLoading(false)
                    )
                )
            }
        }
    }

    // Helper for sending UI events (snackbars)
    private suspend fun sendSnackbarWithAction(messageRes: Int, action: (() -> Unit)? = null) {
        _effects.send(
            element = UiEffect.ShowSnackbarWithAction(
                messageRes = messageRes,
                actionLabel = action?.let { CommonStrings.RETRY },
                action = action
            )
        )
    }
}