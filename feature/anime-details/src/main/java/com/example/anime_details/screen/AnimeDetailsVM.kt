package com.example.anime_details.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.auth.delegate.AuthDelegate
import com.example.common.vm_helpers.models.LoadingState
import com.example.common.vm_helpers.utils.toWhileSubscribed
import com.example.data.domain.CollectionsRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.domain.WatchedEpsRepo
import com.example.data.models.collections.request.CollectionItem
import com.example.data.models.collections.request.CollectionRequest
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.favorites.FavoriteItem
import com.example.data.models.favorites.FavoriteRequest
import com.example.data.utils.network.network_caller.onError
import com.example.data.utils.network.network_caller.onSuccess
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

private const val FAVORITES_ANIMATION_DELAY = 2_000L

private const val COLLECTIONS_ANIMATION_DELAY = 1_500L
private const val RETRY = "Retry"

@HiltViewModel
class AnimeDetailsVM @Inject constructor(
    private val authDelegate: AuthDelegate,
    private val releasesRepo: ReleasesRepo,
    private val watchedEpsRepo: WatchedEpsRepo,
    private val favoritesRepo: FavoritesRepo,
    private val collectionsRepo: CollectionsRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): ViewModel(), AuthDelegate by authDelegate {

    private val _state = MutableStateFlow(AnimeDetailsState())
    val state = _state.toWhileSubscribed(AnimeDetailsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observeAuth(
            scope = viewModelScope,
            onUpdate = { authState ->
                _state.update { it.copy(authState = authState) }
            }
        )
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
            is AnimeDetailsIntent.ObserveWatchedEps -> observeWatchedEpisodes(intent.id)
            is AnimeDetailsIntent.AddEpisodeToWatched -> addEpisodeToWatched(intent.episodeIndex)

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

    private fun addEpisodeToWatched(episodeIndex: Int) {
        // Safe call: operate only if anime data is loaded
        val animeId = _state.value.anime?.id ?: return

        viewModelScope.launch(dispatcherIo) {
            watchedEpsRepo.insertWatchedEpisode(
                animeId = animeId,
                episodeIndex = episodeIndex
            )
        }
    }

    // --- Favorites ---
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
            delay(FAVORITES_ANIMATION_DELAY)

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
                actionLabel = action?.let { RETRY },
                action = action
            )
        )
    }
}