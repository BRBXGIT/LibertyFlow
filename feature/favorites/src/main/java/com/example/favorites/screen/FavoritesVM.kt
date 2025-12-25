@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.favorites.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.request.request_parameters.UiShortRequestParameters
import com.example.design_system.components.snackbars.sendRetrySnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoritesVM @Inject constructor(
    authRepo: AuthRepo,
    private val favoritesRepo: FavoritesRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): BaseAuthVM(authRepo, dispatcherIo) {

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
            FavoritesIntent.GetTokens -> {
                getAuthToken(
                    request = UiTokenRequest(_favoritesState.value.email, _favoritesState.value.password),
                    onStart = { _favoritesState.update { it.copy(isLoading = true, isPasswordOrEmailIncorrect = false) } },
                    onSuccess = { _favoritesState.update { it.setLoading(false) } },
                    onIncorrectData = { _favoritesState.update { it.copy(isPasswordOrEmailIncorrect = true) } },
                    onAnyError = { message, retry -> sendRetrySnackbar(message) { retry() } },
                )
            }

            else -> {}
        }
    }

    init {
        observeAuthState { authState ->
            _favoritesState.update { it.setAuthState(authState) }
        }
    }
}