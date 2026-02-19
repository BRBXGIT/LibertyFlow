@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.collections.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.utils.toLazily
import com.example.data.domain.AuthRepo
import com.example.data.domain.CollectionsRepo
import com.example.data.models.auth.TokenRequest
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.ShortRequestParameters
import com.example.data.models.common.anime_item.AnimeItem
import com.example.design_system.utils.CommonStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
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
 * ViewModel responsible for managing the state and logic of the Collections screen.
 * * This VM handles paginated anime lists across different collection types,
 * search functionality with debouncing, and authentication flows via [BaseAuthVM].
 *
 * @property authRepo Repository handling authentication-related data operations.
 * @property collectionsRepo Repository providing access to anime collections and paging data.
 * @property ioDispatcher The [CoroutineDispatcher] used for background operations,
 * injected via [LibertyFlowDispatcher.IO].
 */
@HiltViewModel
class CollectionsVM @Inject constructor(
    authRepo: AuthRepo,
    private val collectionsRepo: CollectionsRepo,
    @Dispatcher(LibertyFlowDispatcher.IO) ioDispatcher: CoroutineDispatcher
): BaseAuthVM(authRepo, ioDispatcher) {

    private val _state = MutableStateFlow(CollectionsState())
    val state = _state.toLazily(CollectionsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeAuthentication()
    }

    /**
     * Syncs the global authentication state with the local UI state.
     */
    private fun observeAuthentication() {
        userAuthState
            .onEach { auth -> _state.update { it.setAuthState(auth) } }
            .launchIn(viewModelScope)
    }

    // --- Data Streams ---
    /**
     * Shared query flow with debounce to optimize search requests.
     */
    private val queryFlow = _state
        .map { it.searchForm.query }
        .distinctUntilChanged()

    /**
     * A map of [Collection] types to their respective [PagingData] flows.
     * * This dynamically generates an independent paging stream for every entry in
     * the [Collection] enum. Each stream reacts to changes in [queryFlow] and
     * is cached within the [viewModelScope].
     */
    val pagingFlows: Map<Collection, Flow<PagingData<AnimeItem>>> =
        Collection.entries.associateWith { collection ->
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
            // --- Ui ---
            is CollectionsIntent.ToggleIsAuthBSVisible ->
                _state.update { it.copy(authForm = it.authForm.toggleIsAuthBSVisible()) }
            is CollectionsIntent.ToggleIsSearching ->
                _state.update { it.copy(searchForm = it.searchForm.toggleSearching()) }
            is CollectionsIntent.UpdateQuery ->
                _state.update { it.copy(searchForm = it.searchForm.updateQuery(intent.query)) }
            is CollectionsIntent.SetCollection ->
                _state.update { it.copy(selectedCollection = intent.collection) }
            is CollectionsIntent.SetIsError ->
                _state.update { it.copy(isError = intent.value) }

            // --- Auth ---
            is CollectionsIntent.UpdateAuthForm -> handleAuthFormUpdate(intent.field)
            is CollectionsIntent.GetTokens -> performLogin()
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    // --- Auth ---
    private fun performLogin() {
        val form = _state.value.authForm

        executeAuthentication(
            request = TokenRequest(form.login, form.password),
            onStart = { setAuthErrorState(isError = false) },
            onValidationError = { setAuthErrorState(isError = true, bsVisible = true) },
            onError = { msg, retry ->
                sendEffect(
                    effect = UiEffect.ShowSnackbarWithAction(
                        messageRes = msg,
                        actionLabel = CommonStrings.RETRY,
                        action = retry
                    )
                )
            }
        )
    }

    private fun setAuthErrorState(isError: Boolean, bsVisible: Boolean = false) =
        _state.update { it.updateAuthForm { f -> f.copy(isError = isError, isAuthBSVisible = bsVisible) } }

    private fun handleAuthFormUpdate(field: CollectionsIntent.UpdateAuthForm.AuthField) {
        _state.update { state ->
            state.updateAuthForm { form ->
                when (field) {
                    is CollectionsIntent.UpdateAuthForm.AuthField.Email -> form.copy(login = field.value)
                    is CollectionsIntent.UpdateAuthForm.AuthField.Password -> form.copy(password = field.value)
                }
            }
        }
    }
}