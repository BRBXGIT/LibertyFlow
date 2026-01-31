@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.collections.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.toLazily
import com.example.data.domain.AuthRepo
import com.example.data.domain.CollectionsRepo
import com.example.data.models.auth.TokenRequest
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.ShortRequestParameters
import com.example.data.models.common.ui_anime_item.AnimeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RETRY = "Retry"

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

    // Shared query flow to avoid recreating it for every collection
    private val queryFlow = _state
        .map { state -> state.searchForm.query }
        .distinctUntilChanged()

    /**
     * Architectural Decision: Dynamic Flow Generation
     * Instead of 5 separate variables, we map each Collection enum entry to its specific PagingData flow.
     * This keeps the flows independent (allowing simultaneous data in the Pager)
     * but removes code duplication.
     */
    val pagingFlows: Map<Collection, Flow<PagingData<AnimeItem>>> = Collection.entries.associateWith { collection ->
        queryFlow.flatMapLatest { query ->
            val request = CommonRequestWithCollectionType(
                requestParameters = ShortRequestParameters(search = query),
                collection = collection
            )
            collectionsRepo.getAnimeInCollection(request)
        }.cachedIn(viewModelScope)
    }

    // --- Intents ---
    fun sendIntent(intent: CollectionsIntent) {
        when (intent) {
            // Ui toggles
            CollectionsIntent.ToggleIsAuthBSVisible -> _state.update { it.copy(authForm = it.authForm.toggleIsAuthBSVisible()) }
            CollectionsIntent.ToggleIsSearching -> _state.update { it.copy(searchForm = it.searchForm.toggleSearching())}

            // Ui sets
            is CollectionsIntent.SetIsError -> _state.update { it.setError(intent.value) }
            is CollectionsIntent.SetCollection -> _state.update { it.setCollection(intent.collection) }

            // Ui updates
            is CollectionsIntent.UpdateQuery -> _state.update { it.copy(searchForm = it.searchForm.updateQuery(intent.query)) }

            // Auth (Logic preserved as requested)
            is CollectionsIntent.UpdateAuthForm -> handleAuthFormUpdate(intent.field)
            CollectionsIntent.GetTokens -> performLogin()
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(effect)
        }
    }

    // --- Auth Logic (Untouched as requested) ---
    private fun performLogin() {
        val currentState = _state.value.authForm
        getAuthToken(
            request = TokenRequest(currentState.email, currentState.password),
            onStart = { _state.update { it.updateAuthForm { f -> f.copy(isError = false) } } },
            onIncorrectData = { _state.update { it.updateAuthForm { f -> f.copy(isError = true) } } },
            onAnyError = { messageRes, retryAction ->
                sendEffect(UiEffect.ShowSnackbarWithAction(messageRes, RETRY, retryAction))
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