@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.favorites.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.auth.AuthDelegate
import com.example.common.vm_helpers.utils.toLazily
import com.example.data.domain.FavoritesRepo
import com.example.data.models.common.request.common_request.CommonRequest
import com.example.data.models.common.request.request_parameters.ShortRequestParameters
import com.example.design_system.utils.CommonStrings
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

/**
 * ViewModel responsible for managing the state and logic of the Favorites screen.
 * * This ViewModel follows a unidirectional data flow (MVI) pattern, handling
 * authentication via [BaseAuthVM], search-based filtering, and paginated data
 * retrieval through [FavoritesRepo].
 *
 * @property favoritesRepo Repository providing access to the user's favorite items.
 * @param ioDispatcher The coroutine dispatcher for background operations.
 */
@HiltViewModel
class FavoritesVM @Inject constructor(
    private val authDelegate: AuthDelegate,
    private val favoritesRepo: FavoritesRepo,
    @Dispatcher(LibertyFlowDispatcher.IO) ioDispatcher: CoroutineDispatcher
): ViewModel(), AuthDelegate by authDelegate {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.toLazily(FavoritesState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeAuth(viewModelScope)
        authState
            .onEach { state -> _state.update { it.copy(authState = state) } }
            .launchIn(viewModelScope)
    }

    // --- Intents ---
    /**
     * Entry point for all UI interactions.
     */
    fun sendIntent(intent: FavoritesIntent) {
        when (intent) {
            // --- Ui ---
            is FavoritesIntent.ToggleIsAuthBSVisible -> toggleAuthBS()
            is FavoritesIntent.ToggleIsSearching ->
                _state.update { it.copy(searchForm = it.searchForm.toggleSearching()) }
            is FavoritesIntent.UpdateQuery ->
                _state.update { it.copy(searchForm = it.searchForm.updateQuery(intent.query)) }
            is FavoritesIntent.SetIsLoading ->
                _state.update { it.copy(loadingState = it.loadingState.withLoading(intent.value)) }
            is FavoritesIntent.SetIsError ->
                _state.update { it.copy(loadingState = it.loadingState.withError(intent.value)) }

            // --- Auth ---
            is FavoritesIntent.UpdateLogin -> onLoginChanged(intent.login)
            is FavoritesIntent.UpdatePassword -> onPasswordChanged(intent.password)
            is FavoritesIntent.GetTokens -> performLogin()
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    // --- Auth ---
    /**
     * Handles login logic using the delegate class helper.
     */
    private fun performLogin() {
        login(
            scope = viewModelScope,
            onError = { msgRes, retry ->
                sendEffect(
                    effect = UiEffect.ShowSnackbarWithAction(
                        messageRes = msgRes,
                        actionLabel = CommonStrings.RETRY,
                        action = retry
                    )
                )
            }
        )
    }

    // --- Data Streams ---
    /**
     * Paging data stream. Automatically re-fetches when search query changes.
     */
    val favorites = _state
        .map { it.searchForm.query }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            favoritesRepo.getFavorites(CommonRequest(ShortRequestParameters(search = query)))
        }
        .cachedIn(viewModelScope)
}