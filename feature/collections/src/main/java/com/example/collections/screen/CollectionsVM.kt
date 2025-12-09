@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.collections.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toLazily
import com.example.common.vm_helpers.updatee
import com.example.data.domain.AuthRepo
import com.example.data.domain.CollectionsRepo
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.common.request.common_request.UiCommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.UiShortRequestParameters
import com.example.data.utils.remote.network_request.NetworkErrors
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
class CollectionsVM @Inject constructor(
    private val collectionsRepo: CollectionsRepo,
    private val authRepo: AuthRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    private val _collectionsState = MutableStateFlow(CollectionsState())
    val collectionsState = _collectionsState.toLazily(CollectionsState())

    private val query = _collectionsState
        .map { state -> state.query }
        .distinctUntilChanged()

    // TODO rewrite to something like sliding window
    /**
     * All collections as paging data flows
     */
    val plannedAnime = query.flatMapLatest { query ->
        val request = UiCommonRequestWithCollectionType(
            requestParameters = UiShortRequestParameters(search = query),
            collection = Collection.PLANNED
        )
        collectionsRepo.getAnimeInCollection(request)
    }.cachedIn(viewModelScope)

    val watchedAnime = query.flatMapLatest { query ->
        val request = UiCommonRequestWithCollectionType(
            requestParameters = UiShortRequestParameters(search = query),
            collection = Collection.WATCHED
        )
        collectionsRepo.getAnimeInCollection(request)
    }.cachedIn(viewModelScope)

    val watchingAnime = query.flatMapLatest { query ->
        val request = UiCommonRequestWithCollectionType(
            requestParameters = UiShortRequestParameters(search = query),
            collection = Collection.WATCHING
        )
        collectionsRepo.getAnimeInCollection(request)
    }.cachedIn(viewModelScope)

    val postponedAnime = query.flatMapLatest { query ->
        val request = UiCommonRequestWithCollectionType(
            requestParameters = UiShortRequestParameters(search = query),
            collection = Collection.POSTPONED
        )
        collectionsRepo.getAnimeInCollection(request)
    }.cachedIn(viewModelScope)

    val abandonedAnime = query.flatMapLatest { query ->
        val request = UiCommonRequestWithCollectionType(
            requestParameters = UiShortRequestParameters(search = query),
            collection = Collection.ABANDONED
        )
        collectionsRepo.getAnimeInCollection(request)
    }.cachedIn(viewModelScope)

    /**
     * Observes authentication state and updates UI.
     */
    private fun observeAuthState() {
        viewModelScope.launch(dispatcherIo) {
            authRepo.authState.collect { authState ->
                _collectionsState.updatee { copy(authState = authState) }
            }
        }
    }

    /**
     * Requests auth token using email/password.
     * Shows snackbar with retry on unknown errors.
     */
    private fun getAuthToken() {
        viewModelScope.launch(dispatcherIo) {
            _collectionsState.updatee {
                copy(
                    isLoading = true,
                    isPasswordOrEmailIncorrect = false
                )
            }

            val request = UiTokenRequest(
                login = _collectionsState.value.email,
                password = _collectionsState.value.password
            )
            authRepo.getToken(request)
                .onSuccess { uiToken ->
                    authRepo.saveToken(uiToken.token!!)
                    _collectionsState.updatee { copy(isLoading = false) }
                }
                .onError { error, message ->
                    if (error == NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD) {
                        _collectionsState.updatee {
                            copy(
                                isLoading = false,
                                isPasswordOrEmailIncorrect = true
                            )
                        }
                    } else {
                        sendRetrySnackbar(message) { getAuthToken() }
                    }
                }
        }
    }

    fun sendIntent(intent: CollectionsIntent) {
        when (intent) {

            // Ui toggles
            CollectionsIntent.ToggleIsAuthBSVisible ->
                _collectionsState.update { it.toggleAuthBS() }

            CollectionsIntent.ToggleIsSearching ->
                _collectionsState.update { it.toggleIsSearching() }

            // Ui sets
            is CollectionsIntent.SetIsLoading ->
                _collectionsState.update { it.setLoading(intent.value) }

            is CollectionsIntent.SetIsError ->
                _collectionsState.update { it.setError(intent.value) }

            is CollectionsIntent.SetCollection ->
                _collectionsState.update { it.setCollection(intent.collection) }

            // Ui updates
            is CollectionsIntent.UpdateQuery ->
                _collectionsState.update { it.updateQuery(intent.query) }

            is CollectionsIntent.UpdateEmail ->
                _collectionsState.update { it.updateEmail(intent.email) }

            is CollectionsIntent.UpdatePassword ->
                _collectionsState.update { it.updatePassword(intent.password) }

            // Actions
            CollectionsIntent.GetTokens ->
                getAuthToken()
        }
    }

    init {
        observeAuthState()
    }
}