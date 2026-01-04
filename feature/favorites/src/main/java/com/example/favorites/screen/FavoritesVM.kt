@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.favorites.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.UiEffect
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.common.request.common_request.UiCommonRequest
import com.example.data.models.common.request.request_parameters.UiShortRequestParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesVM @Inject constructor(
    authRepo: AuthRepo,
    private val favoritesRepo: FavoritesRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO)
    private val dispatcherIo: CoroutineDispatcher
) : BaseAuthVM(authRepo, dispatcherIo) {

    // UI state
    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.toLazily(FavoritesState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // Paging request driven by search query
    private val requestFlow = _state
        .map { UiShortRequestParameters(search = it.query) }
        .distinctUntilChanged()

    val favorites = requestFlow
        .flatMapLatest { request ->
            favoritesRepo.getFavorites(UiCommonRequest(request))
        }
        .cachedIn(viewModelScope)

    /**
     * Entry point for UI events.
     */
    fun sendIntent(intent: FavoritesIntent) {
        when (intent) {

            /* --- UI toggles --- */

            FavoritesIntent.ToggleIsAuthBSVisible ->
                _state.update { it.toggleAuthBS() }

            FavoritesIntent.ToggleIsSearching ->
                _state.update { it.toggleSearching() }

            /* --- Flags --- */

            is FavoritesIntent.SetIsLoading ->
                _state.update { it.withLoading(intent.value) }

            is FavoritesIntent.SetIsError ->
                _state.update { it.withError(intent.value) }

            /* --- Search --- */

            is FavoritesIntent.UpdateQuery ->
                _state.update { it.updateQuery(intent.query) }

            /* --- Auth input --- */

            is FavoritesIntent.UpdateEmail ->
                _state.update { it.updateAuthInput(email = intent.email) }

            is FavoritesIntent.UpdatePassword ->
                _state.update { it.updateAuthInput(password = intent.password) }

            /* --- Auth action --- */

            FavoritesIntent.GetTokens ->
                requestAuthTokens()
        }
    }

    /* --- Auth flow --- */

    private fun requestAuthTokens() {
        val current = _state.value

        getAuthToken(
            request = UiTokenRequest(
                login = current.email,
                password = current.password
            ),
            onStart = {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isPasswordOrEmailIncorrect = false
                    )
                }
            },
            onSuccess = {
                _state.update { it.withLoading(false) }
            },
            onIncorrectData = {
                _state.update {
                    it.copy(isPasswordOrEmailIncorrect = true)
                }
            },
            onAnyError = { messageRes, retry ->
                sendEffect(
                    UiEffect.ShowSnackbar(
                        messageRes = messageRes,
                        actionLabel = "Retry",
                        action = retry
                    )
                )
            }
        )
    }

    fun sendEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(effect)
        }
    }

    init {
        // Sync auth state from BaseAuthVM
        observeAuthState { authState ->
            _state.update { it.withAuthState(authState) }
        }
    }
}