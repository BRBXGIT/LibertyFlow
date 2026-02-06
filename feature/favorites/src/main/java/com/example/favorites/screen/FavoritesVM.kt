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
import com.example.data.models.auth.TokenRequest
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.request.request_parameters.ShortRequestParameters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RETRY = "Retry"

@HiltViewModel
class FavoritesVM @Inject constructor(
    authRepo: AuthRepo,
    private val favoritesRepo: FavoritesRepo,
    @Dispatcher(LibertyFlowDispatcher.IO) ioDispatcher: CoroutineDispatcher
): BaseAuthVM(authRepo, ioDispatcher) {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.toLazily(FavoritesState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeAuthentication()
    }

    // --- Intents ---
    /**
     * Entry point for all UI interactions.
     */
    fun sendIntent(intent: FavoritesIntent) {
        when (intent) {
            // --- Ui ---
            is FavoritesIntent.ToggleIsAuthBSVisible ->
                _state.update { it.toggleAuthBS() }
            is FavoritesIntent.ToggleIsSearching ->
                _state.update { it.copy(searchForm = it.searchForm.toggleSearching()) }
            is FavoritesIntent.UpdateQuery ->
                _state.update { it.copy(searchForm = it.searchForm.updateQuery(intent.query)) }
            is FavoritesIntent.SetIsLoading ->
                _state.update { it.copy(loadingState = it.loadingState.withLoading(intent.value)) }
            is FavoritesIntent.SetIsError ->
                _state.update { it.copy(loadingState = it.loadingState.withError(intent.value)) }

            // --- Auth ---
            is FavoritesIntent.UpdateAuthForm -> updateAuthField(intent.field)
            is FavoritesIntent.GetTokens -> performLogin()
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    // --- Auth ---
    /**
     * Handles login logic using the base class helper.
     */
    private fun performLogin() {
        val form = _state.value.authForm

        executeAuthentication(
            request = TokenRequest(form.email, form.password),
            onStart = { setAuthErrorState(false) },
            onValidationError = { setAuthErrorState(true) },
            onError = { msg, retry ->
                sendEffect(UiEffect.ShowSnackbarWithAction(msg, RETRY, retry))
            }
        )
    }

    private fun setAuthErrorState(isError: Boolean) {
        _state.update { it.updateAuthForm { f -> f.copy(isError = isError) } }
    }

    private fun updateAuthField(field: FavoritesIntent.UpdateAuthForm.AuthField) {
        _state.update { state ->
            state.updateAuthForm { form ->
                when (field) {
                    is FavoritesIntent.UpdateAuthForm.AuthField.Email -> form.copy(email = field.value)
                    is FavoritesIntent.UpdateAuthForm.AuthField.Password -> form.copy(password = field.value)
                }
            }
        }
    }

    /**
     * Reacts to authentication changes and updates the main UI state.
     */
    private fun observeAuthentication() {
        authState
            .onEach { auth -> _state.update { it.copy(authState = auth) } }
            .launchIn(viewModelScope)
    }

    // --- Data Streams ---
    /**
     * Paging data stream. Automatically re-fetches when search query changes.
     * Includes 300ms debounce to avoid excessive API calls during typing.
     */
    val favorites = _state
        .map { it.searchForm.query }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            favoritesRepo.getFavorites(CommonRequest(ShortRequestParameters(search = query)))
        }
        .cachedIn(viewModelScope)
}