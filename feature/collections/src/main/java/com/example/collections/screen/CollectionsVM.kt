@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.collections.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.UiEffect
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

    private val _collectionsState = MutableStateFlow(CollectionsState())
    val collectionsState = _collectionsState.toLazily(CollectionsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

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

    fun sendIntent(intent: CollectionsIntent) {
        when (intent) {

            // Ui toggles
            CollectionsIntent.ToggleIsAuthBSVisible ->
                _collectionsState.update { it.toggleAuthBS() }

            CollectionsIntent.ToggleIsSearching ->
                _collectionsState.update { it.toggleIsSearching() }

            // Ui sets
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
            CollectionsIntent.GetTokens -> {
                getAuthToken(
                    request = UiTokenRequest(_collectionsState.value.email, _collectionsState.value.password),
                    onStart = { _collectionsState.update { it.copy(isPasswordOrEmailIncorrect = false) } },
                    onIncorrectData = { _collectionsState.update { it.copy(isPasswordOrEmailIncorrect = true) } },
                    onAnyError = { messageRes, retry ->
                        sendEffect(
                            effect = UiEffect.ShowSnackbar(
                                messageRes = messageRes,
                                actionLabel = "Retry",
                                action = retry
                            )
                        )
                    },
                )
            }
        }
    }

    fun sendEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(effect)
        }
    }

    init {
        observeAuthState { authState ->
            _collectionsState.update { it.setAuthState(authState) }
        }
    }
}