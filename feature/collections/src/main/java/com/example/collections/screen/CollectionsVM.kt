@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.collections.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.AuthRepo
import com.example.data.domain.CollectionsRepo
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.common.request.common_request.UiCommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
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
class CollectionsVM @Inject constructor(
    authRepo: AuthRepo,
    private val collectionsRepo: CollectionsRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): BaseAuthVM(authRepo, dispatcherIo) {

    private val _state = MutableStateFlow(CollectionsState())
    val state = _state.toLazily(CollectionsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeAuthState { authState ->
            _state.update { it.setAuthState(authState) }
        }
    }

    fun sendEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(effect)
        }
    }

    fun sendIntent(intent: CollectionsIntent) {
        when (intent) {

            // Ui toggles
            CollectionsIntent.ToggleIsAuthBSVisible ->
                _state.update { it.toggleAuthBS() }

            CollectionsIntent.ToggleIsSearching ->
                _state.update { it.toggleIsSearching() }

            // Ui sets
            is CollectionsIntent.SetIsError ->
                _state.update { it.setError(intent.value) }

            is CollectionsIntent.SetCollection ->
                _state.update { it.setCollection(intent.collection) }

            // Ui updates
            is CollectionsIntent.UpdateQuery ->
                _state.update { it.updateQuery(intent.query) }

            // Auth
            is CollectionsIntent.UpdateAuthForm -> handleAuthFormUpdate(intent.field)

            // Actions
            CollectionsIntent.GetTokens -> performLogin()
        }
    }

    private val query = _state
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

    private fun handleAuthFormUpdate(field: CollectionsIntent.UpdateAuthForm.AuthField) {
        _state.update { state ->
            state.updateAuthForm { form ->
                when (field) {
                    is CollectionsIntent.UpdateAuthForm.AuthField.Email -> form.copy(email = field.value)
                    is CollectionsIntent.UpdateAuthForm.AuthField.Password -> form.copy(password = field.value)
                }
            }
        }
    }
}