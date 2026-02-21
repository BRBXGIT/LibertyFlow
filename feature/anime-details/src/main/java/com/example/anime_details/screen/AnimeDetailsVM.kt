package com.example.anime_details.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.auth.component.AuthComponent
import com.example.common.vm_helpers.models.LoadingState
import com.example.common.vm_helpers.utils.toWhileSubscribed
import com.example.data.domain.CollectionsRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.domain.WatchedEpsRepo
import com.example.data.models.auth.UserAuthState
import com.example.data.models.collections.request.CollectionItem
import com.example.data.models.collections.request.CollectionRequest
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.favorites.FavoriteItem
import com.example.data.models.favorites.FavoriteRequest
import com.example.data.utils.network.network_caller.onError
import com.example.data.utils.network.network_caller.onSuccess
import com.example.design_system.utils.CommonAnimationDelays
import com.example.design_system.utils.CommonStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val COLLECTIONS_ANIMATION_DELAY = 1_500L

/**
 * ViewModel for the Anime Details screen, responsible for managing UI state,
 * handling user intents, and orchestrating data flow between repositories and the UI.
 *
 * This ViewModel implements [AuthComponent] via delegation to handle authentication
 * logic (login, token management) seamlessly across different screens.
 *
 * @property authComponent Component handling authentication logic through delegation.
 * @property releasesRepo Repository for fetching detailed anime information.
 * @property watchedEpsRepo Repository for managing and observing watched episode data.
 * @property favoritesRepo Repository for syncing the user's favorite anime list.
 * @property collectionsRepo Repository for managing anime within specific user collections.
 * @property dispatcherIo The coroutine dispatcher used for background operations.
 */
@HiltViewModel
class AnimeDetailsVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authComponent: AuthComponent,
    private val releasesRepo: ReleasesRepo,
    private val watchedEpsRepo: WatchedEpsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val collectionsRepo: CollectionsRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel(), AuthComponent by authComponent {

    private val _state = MutableStateFlow(AnimeDetailsState())
    val state = _state.toWhileSubscribed(AnimeDetailsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeAuth(
            scope = viewModelScope,
            onUpdate = { authState ->
                _state.update { it.copy(authState = authState) }
                if (authState.userAuthState == UserAuthState.LoggedIn) {
                    fetchCollections()
                    fetchFavoritesIds()
                }
            }
        )
        val route = savedStateHandle.toRoute<AnimeDetailsRoute>()
        val animeId = route.animeId
        fetchAnime(animeId)
        observeWatchedEpisodes(animeId)
    }

    // --- Auth ---
    /**
     * Executes login request using credentials from the UI state.
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

    // --- Intents ---
    fun sendIntent(intent: AnimeDetailsIntent) {
        when (intent) {
            // Main data
            is AnimeDetailsIntent.FetchAnime -> fetchAnime(intent.id)

            // Favorites
            AnimeDetailsIntent.AddToFavorite -> toggleFavorite(shouldAdd = true)
            AnimeDetailsIntent.RemoveFromFavorite -> toggleFavorite(shouldAdd = false)

            // Collections
            is AnimeDetailsIntent.ToggleCollection -> toggleCollection(intent.collection)

            // Auth
            AnimeDetailsIntent.GetTokens -> performLogin()

            // UI Toggles
            AnimeDetailsIntent.ToggleIsDescriptionExpanded ->
                _state.update { it.copy(isDescriptionExpanded = !it.isDescriptionExpanded) }
            AnimeDetailsIntent.ToggleIsAuthBSVisible ->
                _state.update { it.copy(authState = it.authState.toggleIsAuthBSVisible()) }
            AnimeDetailsIntent.ToggleCollectionsBSVisible ->
                _state.update { it.copy(collectionsState = it.collectionsState.toggleBS()) }

            // Auth
            is AnimeDetailsIntent.UpdateLogin -> onLoginChanged(intent.login)
            is AnimeDetailsIntent.UpdatePassword -> onPasswordChanged(intent.password)
        }
    }

    // --- Effects ---
    fun sendEffect(effect: UiEffect) =
        viewModelScope.launch { _effects.send(effect) }

    // --- Data ---
    private fun fetchAnime(id: Int) {
        viewModelScope.launch(dispatcherIo) {
            _state.update { it.copy(loadingState = LoadingState(isLoading = true, isError = false)) }

            releasesRepo.getAnime(id)
                .onSuccess { uiAnimeDetails ->
                    _state.update { it.copy(anime = uiAnimeDetails, loadingState = it.loadingState.copy(isLoading = false)) }
                }
                .onError { _, messageRes ->
                    _state.update { it.copy(loadingState = LoadingState(isLoading = false, isError = true)) }
                    sendSnackbar(messageRes) { fetchAnime(id) }
                }
        }
    }

    private fun observeWatchedEpisodes(animeId: Int) {
        viewModelScope.launch(dispatcherIo) {
            // Ensure title exists before observing
            watchedEpsRepo.insertTitle(animeId)
            watchedEpsRepo.getWatchedEpisodes(animeId).collect { episodes ->
                _state.update { it.copy(watchedEps = episodes) }
            }
        }
    }

    // --- Favorites ---
    /**
     * Fetches the list of favorite anime IDs for the current user.
     */
    private fun fetchFavoritesIds() {
        viewModelScope.launch(dispatcherIo) {
            _state.update {
                it.copy(
                    favoritesState = it.favoritesState.copy(
                        loadingState = it.favoritesState.loadingState.withBoth(
                            loading = true,
                            error = false
                        )
                    )
                )
            }

            favoritesRepo.getFavoritesIds()
                .onSuccess { ids ->
                    _state.update {
                        it.copy(
                            favoritesState = it.favoritesState.copy(
                                ids = ids,
                                loadingState = it.favoritesState.loadingState.withLoading(false)
                            )
                        )
                    }
                }
                .onError { _, messageRes ->
                    _state.update {
                        it.copy(
                            favoritesState = it.favoritesState.copy(
                                loadingState = it.favoritesState.loadingState.withBoth(
                                    loading = false,
                                    error = true
                                )
                            )
                        )
                    }
                    sendSnackbar(messageRes) { fetchFavoritesIds() }
                }
        }
    }

    /**
     * Adds or removes the current anime from the user's favorites.
     * @param shouldAdd True to add to favorites, false to remove.
     */
    private fun toggleFavorite(shouldAdd: Boolean) {
        val animeId = _state.value.anime?.id ?: return

        viewModelScope.launch(dispatcherIo) {
            _state.update {
                it.copy(
                    favoritesState = it.favoritesState.copy(
                        loadingState = it.favoritesState.loadingState.withBoth(
                            loading = true,
                            error = false
                        )
                    )
                )
            }

            // Construct request
            val request = FavoriteRequest().apply { add(FavoriteItem(animeId)) }

            // Just cause i want to show animation :)
            delay(CommonAnimationDelays.RAINBOW_BUTTON_ANIMATION_DELAY)

            val result = if (shouldAdd) favoritesRepo.addFavorite(request) else favoritesRepo.deleteFavorite(request)

            result
                .onSuccess {
                    if (shouldAdd) _state.update { it.addAnimeToFavorites() } else _state.update { it.removeAnimeFromFavorites() }
                }
                .onError { _, messageRes ->
                    _state.update {
                        it.copy(
                            favoritesState = it.favoritesState.copy(
                                loadingState = it.favoritesState.loadingState.withBoth(
                                    loading = false,
                                    error = true
                                )
                            )
                        )
                    }

                    sendSnackbar(messageRes) { toggleFavorite(shouldAdd) }
                }
        }
    }

    // --- Collections ---
    /**
     * Fetches all collections available for the current user.
     */
    private fun fetchCollections() {
        viewModelScope.launch(dispatcherIo) {
            _state.update { it.copy(collectionsState = it.collectionsState.copy(
                loadingState = it.collectionsState.loadingState.withBoth(loading = true, error = false)
            ))}

            // Just cause i want to show animation :)
            delay(COLLECTIONS_ANIMATION_DELAY)

            collectionsRepo.getCollectionsIds()
                .onSuccess { collections ->
                    _state.update { it.copy(
                        collectionsState = it.collectionsState.copy(
                            collections = collections,
                            loadingState = it.collectionsState.loadingState.withLoading(false)
                        )
                    )}
                }
                .onError { _, messageRes ->
                    _state.update { it.copy(
                        collectionsState = it.collectionsState.copy(
                            loadingState = it.collectionsState.loadingState.withBoth(loading = false, error = true)
                        )
                    )}
                    sendSnackbar(messageRes) { fetchCollections() }
                }
        }
    }

    /**
     * Toggles the presence of the current anime in a specific collection.
     * @param targetType The type of collection (e.g., 'Watching', 'Planned').
     */
    private fun toggleCollection(targetType: Collection) {
        val animeId = _state.value.anime?.id ?: return
        val currentActive = _state.value.activeCollection

        viewModelScope.launch(dispatcherIo) {
            if (currentActive == targetType) {
                handleCollectionAction(animeId, targetType, isAdding = false)
            }
            else {
                handleCollectionAction(animeId, targetType, isAdding = true)
            }
        }
    }

    /**
     * Core logic for adding/removing items from a collection with loading state management.
     * @param animeId The ID of the anime.
     * @param type The target [Collection].
     * @param isAdding Whether the item is being added or removed.
     */
    private suspend fun handleCollectionAction(animeId: Int, type: Collection, isAdding: Boolean) {
        _state.update { it.copy(collectionsState = it.collectionsState.copy(
            loadingState = it.collectionsState.loadingState.withBoth(loading = true, error = false)
        ))}

        // Just cause i want to show animation :)
        delay(COLLECTIONS_ANIMATION_DELAY)

        val request = CollectionRequest().apply { add(CollectionItem(animeId, type)) }
        val result = if (isAdding) collectionsRepo.addToCollection(request)
        else collectionsRepo.deleteFromCollection(request)

        result
            .onSuccess {
                _state.update { it.updateCollection(type, isAdding) }
            }
            .onError { _, messageRes ->
                _state.update { it.copy(collectionsState = it.collectionsState.copy(
                    loadingState = it.collectionsState.loadingState.withBoth(loading = false, error = true)
                ))}
                sendSnackbar(messageRes) { toggleCollection(type) }
            }
    }

    // Helper to reduce boilerplate for effects
    private fun sendSnackbar(messageRes: Int, action: (() -> Unit)? = null) {
        sendEffect(
            effect = UiEffect.ShowSnackbarWithAction(
                messageRes = messageRes,
                actionLabel = action?.let { CommonStrings.RETRY },
                action = action
            )
        )
    }
}