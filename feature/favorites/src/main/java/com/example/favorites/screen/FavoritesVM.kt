@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.favorites.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.common.request.common_request.UiCommonRequest
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
class FavoritesVM @Inject constructor(
    private val favoritesRepo: FavoritesRepo,
    private val authRepo: AuthRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel() {

    // UI state
    private val _favoritesState = MutableStateFlow(FavoritesState())
    val favoritesState = _favoritesState.toLazily(FavoritesState())

    // Paging: Favorites
    private val requestParameters = _favoritesState
        .map { UiShortRequestParameters(search = it.query) }
        .distinctUntilChanged()

    val favorites = requestParameters
        .flatMapLatest { request ->
            favoritesRepo.getFavorites(UiCommonRequest(request))
        }
        .cachedIn(viewModelScope)

    /**
     * Observes authentication state and updates UI.
     */
    private fun observeAuthState() {
        viewModelScope.launch(dispatcherIo) {
            authRepo.authState.collect { authState ->
                _favoritesState.update { it.setAuthState(authState) }
            }
        }
    }

    /**
     * Requests auth token using email/password.
     * Shows snackbar with retry on unknown errors.
     */
    private fun getAuthToken() {
        viewModelScope.launch(dispatcherIo) {
            _favoritesState.update { it.copy(isLoading = true, isPasswordOrEmailIncorrect = false) }

            val request = UiTokenRequest(
                login = _favoritesState.value.email,
                password = _favoritesState.value.password
            )
            authRepo.getToken(request)
                .onSuccess { uiToken ->
                    authRepo.saveToken(uiToken.token!!)
                    _favoritesState.update { it.setLoading(false) }
                }
                .onError { error, message ->
                    if (error == NetworkErrors.INCORRECT_EMAIL_OR_PASSWORD) {
                        _favoritesState.update { it.copy(isLoading = false, isPasswordOrEmailIncorrect = true) }
                    } else {
                        sendRetrySnackbar(message) { getAuthToken() }
                    }
                }
        }
    }

    /**
     * Handles UI intents.
     */
    fun sendIntent(intent: FavoritesIntent) {
        when (intent) {

            // Ui toggles
            FavoritesIntent.ToggleIsAuthBSVisible ->
                _favoritesState.update { it.toggleAuthBS() }

            FavoritesIntent.ToggleIsSearching ->
                _favoritesState.update { it.toggleIsSearching() }

            // Ui sets
            is FavoritesIntent.SetIsLoading ->
                _favoritesState.update { it.setLoading(intent.value) }

            is FavoritesIntent.SetIsError ->
                _favoritesState.update { it.setError(intent.value) }

            // Ui updates
            is FavoritesIntent.UpdateQuery ->
                _favoritesState.update { it.updateQuery(intent.query) }

            is FavoritesIntent.UpdateEmail ->
                _favoritesState.update { it.updateEmail(intent.email) }

            is FavoritesIntent.UpdatePassword ->
                _favoritesState.update { it.updatePassword(intent.password) }

            // Actions
            FavoritesIntent.GetTokens ->
                getAuthToken()
        }
    }

    init {
        observeAuthState()
    }
}