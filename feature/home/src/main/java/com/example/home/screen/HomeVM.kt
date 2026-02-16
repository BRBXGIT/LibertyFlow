@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.CatalogRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.utils.network.network_caller.onError
import com.example.data.utils.network.network_caller.onSuccess
import com.example.design_system.utils.CommonAnimationDelays
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
 * ViewModel for the Home/Discovery screen, following the MVI (Model-View-Intent) pattern.
 *
 * This ViewModel manages the state of anime discovery, including complex filtering
 * (genres, seasons, years, and statuses), paginated search results, and metadata
 * fetching (genres list). It leverages StateFlow for UI state and [Channel] for
 * one-time events (Effects).
 *
 * ### Key Responsibilities:
 * - **State Management:** Maintains [HomeState] which encapsulates UI visibility,
 * loading states, and current filter configurations.
 * - **Reactive Filtering:** Observes changes in the filter request and automatically
 * triggers new PagingData streams via [catalogRepo].
 * - **Side Effects:** Handles navigation and snackbar messages through a [UiEffect] flow.
 * - **Business Logic:** Encapsulates random anime selection and genre synchronization.
 *
 * @property releasesRepo Repository for fetching general release data and random selections.
 * @property catalogRepo Repository for searching and paging through the anime catalog.
 * @property genresRepo Repository for managing available anime genres.
 * @property dispatcherIo The coroutine dispatcher used for background operations,
 */
@HiltViewModel
class HomeVM @Inject constructor(
    private val releasesRepo: ReleasesRepo,
    private val catalogRepo: CatalogRepo,
    private val genresRepo: GenresRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.toLazily(HomeState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // --- Intents ---
    fun sendIntent(intent: HomeIntent) {
        when (intent) {
            // UI Visibility state
            HomeIntent.ToggleSearching ->
                _state.update { it.toggleSearching() }
            HomeIntent.ToggleFiltersBottomSheet ->
                _state.update { it.copy(filtersState = it.filtersState.toggleBS()) }

            // Filter mutations
            is HomeIntent.UpdateQuery ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { updateSearch(intent.query) }) }
            is HomeIntent.UpdateSorting ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { updateSorting(intent.sorting) }) }
            is HomeIntent.ToggleIsOngoing -> handleToggleOngoing()

            // Genre management
            is HomeIntent.AddGenre ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { addGenre(intent.genre) }) }
            is HomeIntent.RemoveGenre ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { removeGenre(intent.genre) }) }

            // Season & Year filters
            is HomeIntent.AddSeason ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { addSeason(intent.season) }) }
            is HomeIntent.RemoveSeason ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { removeSeason(intent.season) }) }
            is HomeIntent.UpdateFromYear ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { updateYear(from = intent.year) }) }
            is HomeIntent.UpdateToYear ->
                _state.update { it.copy(filtersState = it.filtersState.updateRequest { updateYear(to = intent.year) }) }

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

    // --- Ongoing logic ---
    private fun handleToggleOngoing() {
        _state.update { state ->
            val isCurrentlyEmpty = state.filtersState.request.publishStatuses.isEmpty()
            state.copy(
                filtersState = state.filtersState.updateRequest {
                    toggleIsOngoing(if (isCurrentlyEmpty) listOf(PublishStatus.IS_ONGOING) else emptyList())
                }
            )
        }
    }

    // --- Data Streams ---
    // Observes request changes and triggers new paging data
    private val requestFlow = _state
        .map { it.filtersState.request }
        .distinctUntilChanged()

    val anime = requestFlow
        .flatMapLatest { request ->
            catalogRepo.getAnimeByQuery(CommonRequest(request))
        }
        .cachedIn(viewModelScope)

    // --- Private Business Logic ---
    private fun getRandomAnime() {
        viewModelScope.launch(dispatcherIo) {
            // Reset state and show loading
            _state.update { it.copy(randomAnimeState = it.randomAnimeState.withBoth(loading = true, error = false)) }

            delay(CommonAnimationDelays.RAINBOW_BUTTON_ANIMATION_DELAY) // Artificial delay for animation polish

            releasesRepo.getRandomAnime()
                .onSuccess { anime ->
                    sendEffect(UiEffect.Navigate(AnimeDetailsRoute(anime.id)))
                }
                .onError { _, messageRes ->
                    _state.update { it.copy(randomAnimeState = it.randomAnimeState.withError(true)) }
                    sendSnackbar(messageRes) { getRandomAnime() }
                }

            _state.update { it.copy(randomAnimeState = it.randomAnimeState.withLoading(false)) }
        }
    }

    private fun getGenres() {
        viewModelScope.launch(dispatcherIo) {
            _state.update { it.copy(genresState = it.genresState.copy(loadingState = it.genresState.loadingState.withLoading(true))) }

            genresRepo.getGenres()
                .onSuccess { genres -> _state.update { it.copy(genresState = it.genresState.withGenres(genres)) } }
                .onError { _, messageRes -> sendSnackbar(messageRes) { getGenres() } }

            _state.update { it.copy(genresState = it.genresState.copy(loadingState = it.genresState.loadingState.withLoading(false))) }
        }
    }

    // Helper for sending UI events (snackbars, navigation)
    private suspend fun sendSnackbar(messageRes: Int, action: (() -> Unit)? = null) {
        _effects.send(
            UiEffect.ShowSnackbarWithAction(
                messageRes = messageRes,
                actionLabel = action?.let { CommonStrings.RETRY },
                action = action
            )
        )
    }
}