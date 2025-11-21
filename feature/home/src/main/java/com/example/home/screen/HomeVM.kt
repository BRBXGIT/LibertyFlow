@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toLazily
import com.example.common.vm_helpers.update
import com.example.data.domain.CatalogRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.models.common.request.common_request.UiCommonRequest
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val releasesRepo: ReleasesRepo,
    private val catalogRepo: CatalogRepo,
    private val genresRepo: GenresRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher,
): ViewModel() {

    // UI state exposed as StateFlow
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.toLazily(HomeState())

    // Extract search query and create request parameters whenever query changes
    private val requestParameters = _homeState
        .map { it.request }
        .distinctUntilChanged()

    // Paging flow: fired whenever requestParameters changes
    val anime = requestParameters
        .flatMapLatest { request ->
            catalogRepo.getAnimeByQuery(UiCommonRequest(request))
        }
        .cachedIn(viewModelScope)

    /**
     * Loads random anime ID.
     * Used for "Show random" button.
     */
    private fun getRandomAnime() {
        viewModelScope.launch(dispatcherIo) {
            // Reset error state before sending request
            _homeState.update { copy(isLoading = true) }

            releasesRepo.getRandomAnime()
                .onSuccess { randomAnime ->
                    _homeState.update { copy(randomAnimeId = randomAnime.id, isLoading = false) }
                }
                .onError { _, message ->
                    _homeState.update { copy(isLoading = false) }
                    sendRetrySnackbar(message) { getRandomAnime() }
                }
        }
    }

    /**
     * Loads genres.
     * Used for filters.
     */
    private fun getGenres() {
        viewModelScope.launch(dispatcherIo) {
            _homeState.update { copy(isGenresLoading = true) }

            genresRepo.getGenres()
                .onSuccess { genres ->
                    _homeState.update { copy(isGenresLoading = false, genres = genres) }
                }
                .onError { _, message ->
                    _homeState.update { copy(isGenresLoading = false) }
                    sendRetrySnackbar(message) { getGenres() }
                }
        }
    }

    fun sendIntent(intent: HomeIntent) {
        when (intent) {
            // UI simple state updates
            HomeIntent.UpdateIsSearching ->
                _homeState.update { copy(isSearching = !isSearching) }

            HomeIntent.UpdateIsFiltersBSVisible ->
                _homeState.update { copy(isFiltersBSVisible = !isFiltersBSVisible) }

            is HomeIntent.UpdateIsLoading ->
                _homeState.update { copy(isLoading = intent.isLoading) }

            is HomeIntent.UpdateIsError ->
                _homeState.update { copy(isError = intent.isError) }

            is HomeIntent.UpdateQuery ->
                _homeState.update { copy(request = request.copy(search = intent.query)) }

            is HomeIntent.AddGenre ->
                _homeState.update { copy(request = request.copy(genres = request.genres + intent.genre)) }

            is HomeIntent.RemoveGenre ->
                _homeState.update { copy(request = request.copy(genres = request.genres - intent.genre)) }

            // Data
            HomeIntent.GetRandomAnime -> getRandomAnime()
            HomeIntent.GetGenres -> getGenres()
        }
    }
}