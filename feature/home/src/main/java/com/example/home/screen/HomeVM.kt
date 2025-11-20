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
    val animeByQuery = requestParameters
        .flatMapLatest { request ->
            catalogRepo.getAnimeByQuery(UiCommonRequest(request))
        }
        .cachedIn(viewModelScope)

    /**
     * Fetches latest anime releases from repository.
     * If error happens, viewModel displays retry snackbar.
     */
    private fun fetchLatestReleases() {
        viewModelScope.launch(dispatcherIo) {
            // Reset error state before sending request
            _homeState.update { copy(isError = false, isLoading = true) }

            releasesRepo.getLatestAnimeReleases()
                .onSuccess { uiAnimeItems ->
                    _homeState.update { copy(latestReleases = uiAnimeItems, isLoading = false) }
                }
                .onError { _, message ->
                    _homeState.update { copy(isError = true, isLoading = false) }
                    sendRetrySnackbar(message) { fetchLatestReleases() }
                }
        }
    }

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

    fun sendIntent(intent: HomeIntent) {
        when (intent) {

            // UI simple state updates
            HomeIntent.UpdateIsSearching ->
                _homeState.update { copy(isSearching = !isSearching) }

            is HomeIntent.UpdateIsLoading ->
                _homeState.update { copy(isLoading = intent.isLoading) }

            is HomeIntent.UpdateIsError ->
                _homeState.update { copy(isError = intent.isError) }

            is HomeIntent.UpdateQuery ->
                _homeState.update { copy(request = request.copy(search = intent.query)) }

            // Data
            HomeIntent.GetRandomAnime -> getRandomAnime()
        }
    }

    init {
        // Load latest releases immediately when VM is created
        fetchLatestReleases()
    }
}