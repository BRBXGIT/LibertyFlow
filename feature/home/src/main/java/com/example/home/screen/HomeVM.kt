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
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
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

private const val RETRY = "Retry"
private const val ANIMATION_DELAY = 2_000L

@HiltViewModel
class HomeVM @Inject constructor(
    private val releasesRepo: ReleasesRepo,
    private val catalogRepo: CatalogRepo,
    private val genresRepo: GenresRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.toLazily(HomeState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // --- Intents ---
    fun sendIntent(intent: HomeIntent) {
        when (intent) {
            // Toggles
            HomeIntent.ToggleSearching ->
                _state.update { it.toggleSearching() }
            HomeIntent.ToggleFiltersBottomSheet ->
                _state.update { it.toggleFilters() }
            HomeIntent.ToggleIsOngoing ->
                _state.update {
                    val enabled = it.request.publishStatuses.isEmpty()
                    it.updateRequest {
                        toggleIsOngoing(
                            if (enabled) listOf(PublishStatus.IS_ONGOING) else emptyList()
                        )
                    }
                }

            // Sets
            is HomeIntent.SetLoading ->
                _state.update { it.copy(loadingState = it.loadingState.withLoading(intent.value)) }
            is HomeIntent.SetError ->
                _state.update { it.copy(loadingState = it.loadingState.withError(intent.value)) }

            // Updates
            is HomeIntent.UpdateQuery ->
                _state.update { it.updateRequest { updateSearch(intent.query) } }
            is HomeIntent.AddGenre ->
                _state.update { it.updateRequest { addGenre(intent.genre) } }
            is HomeIntent.RemoveGenre ->
                _state.update { it.updateRequest { removeGenre(intent.genre) } }
            is HomeIntent.AddSeason ->
                _state.update { it.updateRequest { addSeason(intent.season) } }
            is HomeIntent.RemoveSeason ->
                _state.update { it.updateRequest { removeSeason(intent.season) } }
            is HomeIntent.UpdateFromYear ->
                _state.update { it.updateRequest { updateYear(from = intent.year) } }
            is HomeIntent.UpdateToYear ->
                _state.update { it.updateRequest { updateYear(to = intent.year) } }
            is HomeIntent.UpdateSorting ->
                _state.update { it.updateRequest { updateSorting(intent.sorting) } }

            // Data
            HomeIntent.GetRandomAnime -> getRandomAnime()
            HomeIntent.GetGenres -> getGenres()
        }
    }

    fun sendEffect(effect: UiEffect) = viewModelScope.launch(dispatcherIo) {
        _effects.send(effect)
    }

    // Emits whenever request parameters change
    private val requestFlow = _state
        .map { it.request }
        .distinctUntilChanged()

    // Paged anime list driven by requestFlow
    val anime = requestFlow
        .flatMapLatest { request ->
            catalogRepo.getAnimeByQuery(CommonRequest(request))
        }
        .cachedIn(viewModelScope)

    private fun getRandomAnime() {
        viewModelScope.launch(dispatcherIo) {
            _state.update {
                it.copy(
                    randomAnimeState = it.randomAnimeState.withBoth(
                        loading = true,
                        error = false
                    )
                )
            }
            delay(ANIMATION_DELAY) // Just cause i want to show animation
            releasesRepo.getRandomAnime()
                .onSuccess { anime ->
                    sendEffect(
                        UiEffect.Navigate(
                            AnimeDetailsRoute(anime.id)
                        )
                    )
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
            _state.update { it.withGenresLoading(true) }

            genresRepo.getGenres()
                .onSuccess { genres -> _state.update { it.updateGenres(genres) } }
                .onError { _, messageRes -> sendSnackbar(messageRes) { getGenres() } }

            _state.update { it.withGenresLoading(false) }
        }
    }

    // Helper to reduce boilerplate for effects
    private fun sendSnackbar(messageRes: Int, action: (() -> Unit)? = null) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(
                UiEffect.ShowSnackbar(
                    messageRes = messageRes,
                    actionLabel = action?.let { RETRY },
                    action = action
                )
            )
        }
    }
}