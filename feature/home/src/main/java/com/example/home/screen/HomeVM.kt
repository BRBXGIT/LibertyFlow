@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.CatalogRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.design_system.components.snackbars.sendRetrySnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val releasesRepo: ReleasesRepo,
    private val catalogRepo: CatalogRepo,
    private val genresRepo: GenresRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.toLazily(HomeState())

    /**
     * Emits request parameters whenever filters/search change.
     */
    private val requestParameters = _homeState
        .map { it.request }
        .distinctUntilChanged()

    /**
     * Paged anime list, updates automatically on request changes.
     */
    val anime = requestParameters
        .flatMapLatest { request ->
            catalogRepo.getAnimeByQuery(UiCommonRequest(request))
        }
        .cachedIn(viewModelScope)

    private fun getRandomAnime() {
        viewModelScope.launch(dispatcherIo) {
            _homeState.update { it.setLoading(true) }

            releasesRepo.getRandomAnime()
                .onSuccess { anime ->
                    _homeState.update { it.copy(randomAnimeId = anime.id, isLoading = false) }
                }
                .onError { _, message ->
                    _homeState.update { it.setLoading(false) }
                    sendRetrySnackbar(message) { getRandomAnime() }
                }
        }
    }

    private fun getGenres() {
        viewModelScope.launch(dispatcherIo) {
            _homeState.update { it.setGenresLoading(true) }

            genresRepo.getGenres()
                .onSuccess { genres ->
                    _homeState.update { it.copy(genres = genres, isGenresLoading = false) }
                }
                .onError { _, message ->
                    _homeState.update { it.setGenresLoading(false) }
                    sendRetrySnackbar(message) { getGenres() }
                }
        }
    }

    fun sendIntent(intent: HomeIntent) {
        when (intent) {

            /* --- UI toggles --- */
            HomeIntent.ToggleSearching ->
                _homeState.update { it.toggleIsSearching() }

            HomeIntent.ToggleFiltersBottomSheet ->
                _homeState.update { it.toggleFiltersBottomSheet() }


            /* --- UI flags --- */
            is HomeIntent.SetLoading ->
                _homeState.update { it.setLoading(intent.value) }

            is HomeIntent.SetError ->
                _homeState.update { it.setError(intent.value) }

            /* --- Query change --- */
            is HomeIntent.UpdateQuery ->
                _homeState.update { it.updateQuery(intent.query) }

            /* --- Filters --- */
            is HomeIntent.AddGenre ->
                _homeState.update { it.addGenre(intent.genre) }

            is HomeIntent.RemoveGenre ->
                _homeState.update { it.removeGenre(intent.genre) }

            is HomeIntent.AddSeason ->
                _homeState.update { it.addSeason(intent.season) }

            is HomeIntent.RemoveSeason ->
                _homeState.update { it.removeSeason(intent.season) }

            is HomeIntent.UpdateFromYear ->
                _homeState.update { it.updateFromYear(intent.year) }

            is HomeIntent.UpdateToYear ->
                _homeState.update { it.updateToYear(intent.year) }

            is HomeIntent.UpdateSorting ->
                _homeState.update { it.updateSorting(intent.sorting) }

            is HomeIntent.ToggleIsOngoing -> {
                if(_homeState.value.request.publishStatuses.isEmpty()) {
                    _homeState.update { it.toggleIsOngoing(listOf(PublishStatus.IS_ONGOING)) }
                } else {
                    _homeState.update { it.toggleIsOngoing(emptyList()) }
                }
            }

            /* --- Data operations --- */
            HomeIntent.GetRandomAnime -> getRandomAnime()
            HomeIntent.GetGenres -> getGenres()

            else -> {}
        }
    }
}