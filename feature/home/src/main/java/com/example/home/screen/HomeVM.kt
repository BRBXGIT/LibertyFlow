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
import com.example.data.models.common.request.request_parameters.UiFullRequestParameters
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import com.example.design_system.components.snackbars.sendRetrySnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
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

    init {
        fetchLatestReleases()
    }

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.toLazily(HomeState())

    private val requestParameters = _homeState
        .map { UiFullRequestParameters(search = it.query) }
        .distinctUntilChanged()

    val animeByQuery = requestParameters
        .flatMapLatest { request ->
            catalogRepo.getAnimeByQuery(UiCommonRequest(request))
        }.cachedIn(viewModelScope)

    private fun fetchLatestReleases() {
        viewModelScope.launch(dispatcherIo) {
            releasesRepo.getLatestAnimeReleases()
                .onSuccess { uiAnimeItems -> _homeState.update { copy(latestReleases = uiAnimeItems) }}
                .onError { _, message -> sendRetrySnackbar(message) { fetchLatestReleases() } }
        }
    }

    private fun getRandomAnime() {
        viewModelScope.launch(dispatcherIo) {
            releasesRepo.getRandomAnime()
                .onSuccess { randomAnime -> _homeState.update { copy(randomAnimeId = randomAnime.id) } }
                .onError { _, message -> sendRetrySnackbar(message) { getRandomAnime() } }
        }
    }

    fun sendIntent(intent: HomeIntent) {
        when(intent) {
            HomeIntent.GetRandomAnime -> getRandomAnime()

            HomeIntent.UpdateIsSearching -> _homeState.update { copy(isSearching = !isSearching) }
            is HomeIntent.UpdateQuery -> _homeState.update { copy(query = intent.query) }

            is HomeIntent.UpdateIsLoading -> _homeState.update { copy(isLoading = intent.isLoading) }
        }
    }
}