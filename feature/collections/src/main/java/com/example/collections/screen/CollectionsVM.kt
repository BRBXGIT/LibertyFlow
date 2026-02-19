@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.collections.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.auth.delegate.AuthDelegate
import com.example.common.vm_helpers.filters.delegate.FiltersDelegate
import com.example.common.vm_helpers.utils.toLazily
import com.example.data.domain.CollectionsRepo
import com.example.data.models.common.anime_item.AnimeItem
import com.example.data.models.common.request.common_request.CommonRequestWithCollectionType
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.request.request_parameters.ShortRequestParameters
import com.example.design_system.utils.CommonStrings
import dagger.hilt.android.lifecycle.HiltViewModel
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

/**
 * ViewModel for the Collections screen, managing different user anime lists
 * (e.g., Watching, Completed) with integrated search and authentication.
 *
 * This class utilizes dual delegation:
 * - [AuthDelegate]: Handles user authentication and login visibility state.
 * - [FiltersDelegate]: Handles search query logic and visibility of the search bar.
 *
 * @property authDelegate Delegate for authentication operations.
 * @property filtersDelegate Delegate for search and filter state management.
 * @property collectionsRepo Repository providing paginated data for specific user collections.
 */
@HiltViewModel
class CollectionsVM @Inject constructor(
    private val filtersDelegate: FiltersDelegate,
    private val authDelegate: AuthDelegate,
    private val collectionsRepo: CollectionsRepo,
): ViewModel(),
    AuthDelegate by authDelegate,
    FiltersDelegate by filtersDelegate
{
    private val _state = MutableStateFlow(CollectionsState())
    val state = _state.toLazily(CollectionsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeAuth(
            scope = viewModelScope,
            onUpdate = { authState ->
                _state.update { it.copy(authState = authState) }
            }
        )
        observeFilters(
            scope = viewModelScope,
            onUpdate = { filtersState ->
                _state.update { it.copy(filtersState = filtersState) }
            }
        )
    }

    // --- Intents ---
    fun sendIntent(intent: CollectionsIntent) {
        when (intent) {
            // --- Ui ---
            is CollectionsIntent.ToggleIsAuthBSVisible -> toggleAuthBS()
            is CollectionsIntent.ToggleIsSearching -> toggleIsSearching()
            is CollectionsIntent.UpdateQuery -> updateQuery(intent.query)
            is CollectionsIntent.SetCollection ->
                _state.update { it.copy(selectedCollection = intent.collection) }

            // --- Auth ---
            is CollectionsIntent.UpdatePassword -> onPasswordChanged(intent.password)
            is CollectionsIntent.UpdateLogin -> onLoginChanged(intent.login)
            is CollectionsIntent.GetTokens -> performLogin()
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
     * Shared query flow with debounce to optimize search requests.
     */
    private val queryFlow = _state
        .map { it.filtersState.requestParameters.search }
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
}