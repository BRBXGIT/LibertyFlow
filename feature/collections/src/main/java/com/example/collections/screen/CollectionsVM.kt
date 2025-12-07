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

    private val collectionParameters = _collectionsState
        .map { state -> CollectionParameters(query = state.query, selectedCollection = state.selectedCollection) }
        .distinctUntilChanged()

    val collectionAnime = collectionParameters.flatMapLatest { parameters ->
        val request = UiCommonRequestWithCollectionType(
            requestParameters = UiShortRequestParameters(search = parameters.query),
            collectionType = parameters.selectedCollection
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
                _collectionsState.updatee { copy(isAuthBSVisible = !isAuthBSVisible) }

            CollectionsIntent.ToggleIsSearching ->
                _collectionsState.updatee { copy(isSearching = !isSearching) }

            // Ui sets
            is CollectionsIntent.SetIsLoading ->
                _collectionsState.updatee { copy(isLoading = intent.isLoading) }

            is CollectionsIntent.SetIsError ->
                _collectionsState.updatee { copy(isError = intent.isError) }
            
            is CollectionsIntent.SetCollection ->
                _collectionsState.updatee { copy(selectedCollection = intent.collection) }

            // Ui updates
            is CollectionsIntent.UpdateQuery ->
                _collectionsState.updatee { copy(query = intent.query) }

            is CollectionsIntent.UpdateEmail ->
                _collectionsState.updatee { copy(email = intent.email) }

            is CollectionsIntent.UpdatePassword ->
                _collectionsState.updatee { copy(password = intent.password) }

            // Actions
            CollectionsIntent.GetTokens ->
                getAuthToken()
        }
    }

    init {
        observeAuthState()
    }
}