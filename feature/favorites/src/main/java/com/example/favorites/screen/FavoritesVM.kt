@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.favorites.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
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

    init {
        // Sync auth state from BaseAuthVM
        observeAuthState { authState ->
            _state.update { it.withAuthState(authState) }
        }
    }

    // Effects
    fun sendEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(effect)
        }
    }

    // Intents
    fun sendIntent(intent: FavoritesIntent) {
        when (intent) {

            /* --- UI toggles --- */

            FavoritesIntent.ToggleIsAuthBSVisible ->
                _state.update { it.copy(authForm = it.authForm.toggleIsAuthBSVisible()) }

            FavoritesIntent.ToggleIsSearching ->
                _state.update { it.copy(searchForm = it.searchForm.toggleSearching()) }

            /* --- Flags --- */

            is FavoritesIntent.SetIsLoading ->
                _state.update { it.copy(loadingState = it.loadingState.withLoading(intent.value)) }

            is FavoritesIntent.SetIsError ->
                _state.update { it.copy(loadingState = it.loadingState.withError(intent.value)) }

            /* --- Search --- */

            is FavoritesIntent.UpdateQuery ->
                _state.update { it.copy(searchForm = it.searchForm.updateQuery(intent.query)) }

            /* --- Auth input --- */

            is FavoritesIntent.UpdateAuthForm -> handleAuthFormUpdate(intent.field)


            /* --- Auth action --- */

            FavoritesIntent.GetTokens -> performLogin()
        }
    }

    // Paging request driven by search query
    private val requestFlow = _state
        .map { UiShortRequestParameters(search = it.searchForm.query) }
        .distinctUntilChanged()

    val favorites = requestFlow
        .flatMapLatest { request ->
            favoritesRepo.getFavorites(UiCommonRequest(request))
        }
        .cachedIn(viewModelScope)

    /* --- Auth flow --- */

    private fun performLogin() {
        val currentState = _state.value.authForm

        getAuthToken(
            request = UiTokenRequest(currentState.email, currentState.password),
            onStart = {
                _state.update { it.updateAuthForm { f -> f.copy(isError = false) } }
            },
            onIncorrectData = {
                _state.update { it.updateAuthForm { f -> f.copy(isError = true) } }
            },
            onAnyError = { messageRes, retryAction ->
                sendEffect(
                    UiEffect.ShowSnackbar(
                        messageRes = messageRes,
                        actionLabel = "Retry",
                        action = retryAction
                    )
                )
            }
        )
    }

    private fun handleAuthFormUpdate(field: FavoritesIntent.UpdateAuthForm.AuthField) {
        _state.update { state ->
            state.updateAuthForm { form ->
                when (field) {
                    is FavoritesIntent.UpdateAuthForm.AuthField.Email -> form.copy(email = field.value)
                    is FavoritesIntent.UpdateAuthForm.AuthField.Password -> form.copy(password = field.value)
                }
            }
        }
    }
}