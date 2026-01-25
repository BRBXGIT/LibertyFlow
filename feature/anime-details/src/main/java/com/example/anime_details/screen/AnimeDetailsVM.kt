package com.example.anime_details.screen

import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.ui_helpers.loading_state.LoadingState
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.toWhileSubscribed
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.domain.WatchedEpsRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.TokenRequest
import com.example.data.models.favorites.FavoriteItem
import com.example.data.models.favorites.FavoriteRequest
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ANIMATION_DELAY = 2_000L
private const val RETRY = "Retry"

@HiltViewModel
class AnimeDetailsVM @Inject constructor(
    authRepo: AuthRepo,
    private val releasesRepo: ReleasesRepo,
    private val watchedEpsRepo: WatchedEpsRepo,
    private val favoritesRepo: FavoritesRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
) : BaseAuthVM(authRepo, dispatcherIo) {

    private val _state = MutableStateFlow(AnimeDetailsState())
    val state = _state.toWhileSubscribed(AnimeDetailsState())

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        // Observe auth state changes from the BaseVM/Repo
        observeAuthState { authState ->
            _state.update { state ->
                state.copy(authState = authState)
            }
            if (authState is AuthState.LoggedIn) {
                fetchFavoritesIds()
            }
        }
    }

    // --- Effects ---
    fun sendIntent(intent: AnimeDetailsIntent) {
        when (intent) {
            is AnimeDetailsIntent.FetchAnime -> fetchAnime(intent.id)
            is AnimeDetailsIntent.ObserveWatchedEps -> observeWatchedEpisodes(intent.id)
            is AnimeDetailsIntent.AddEpisodeToWatched -> addEpisodeToWatched(intent.episodeIndex)

            // Favorites
            AnimeDetailsIntent.AddToFavorite -> toggleFavorite(shouldAdd = true)
            AnimeDetailsIntent.RemoveFromFavorite -> toggleFavorite(shouldAdd = false)

            // Auth
            AnimeDetailsIntent.GetTokens -> performLogin()

            // UI Toggles
            AnimeDetailsIntent.ToggleIsDescriptionExpanded ->
                _state.update { it.copy(isDescriptionExpanded = !it.isDescriptionExpanded) }
            AnimeDetailsIntent.ToggleIsAuthBsVisible ->
                _state.update { it.copy(authForm = it.authForm.toggleIsAuthBSVisible()) }

            // Form Updates (Consolidated)
            is AnimeDetailsIntent.UpdateAuthForm -> handleAuthFormUpdate(intent.field)
        }
    }

    // --- Intents ---
    fun sendEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(effect)
        }
    }

    // --- Anime ---
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
            _state.update { it.updateFavorites { f -> f.copy(isLoading = true, isError = false) } }

            favoritesRepo.getFavoritesIds()
                .onSuccess { ids ->
                    _state.update {
                        it.updateFavorites { f -> f.copy(ids = ids, isLoading = false) }
                    }
                }
                .onError { _, messageRes ->
                    _state.update {
                        it.updateFavorites { f -> f.copy(isError = true, isLoading = false) }
                    }
                    sendSnackbar(messageRes) { fetchFavoritesIds() }
                }
        }
    }

    private fun toggleFavorite(shouldAdd: Boolean) {
        val animeId = _state.value.anime?.id ?: return

        viewModelScope.launch(dispatcherIo) {
            _state.update { it.updateFavorites { f -> f.copy(isLoading = true, isError = false) } }

            // Construct request
            val request = FavoriteRequest().apply { add(FavoriteItem(animeId)) }

            // Just cause i want to show animation :)
            delay(ANIMATION_DELAY)

            val result = if (shouldAdd) favoritesRepo.addFavorite(request) else favoritesRepo.deleteFavorite(request)

            result
                .onSuccess {
                    _state.update {
                        // Reuse the safe logic from State class
                        it.updateFavorites { f -> f.copy(isLoading = false) }.run {
                            if (shouldAdd) addAnimeToFavorites() else removeAnimeFromFavorites()
                        }
                    }
                }
                .onError { _, messageRes ->
                    _state.update { it.updateFavorites { f -> f.copy(isError = true, isLoading = false) } }
                    sendSnackbar(messageRes) { toggleFavorite(shouldAdd) }
                }
        }
    }

    // --- Auth ---
    private fun performLogin() {
        val currentState = _state.value.authForm

        getAuthToken(
            request = TokenRequest(currentState.email, currentState.password),
            onStart = {
                _state.update { it.updateAuthForm { f -> f.copy(isError = false) } }
            },
            onIncorrectData = {
                _state.update { it.updateAuthForm { f -> f.copy(isError = true) } }
            },
            onAnyError = { messageRes, retryAction ->
                sendSnackbar(messageRes, retryAction)
            }
        )
    }

    private fun handleAuthFormUpdate(field: AnimeDetailsIntent.UpdateAuthForm.AuthField) {
        _state.update { state ->
            state.updateAuthForm { form ->
                when (field) {
                    is AnimeDetailsIntent.UpdateAuthForm.AuthField.Email -> form.copy(email = field.value)
                    is AnimeDetailsIntent.UpdateAuthForm.AuthField.Password -> form.copy(password = field.value)
                }
            }
        }
    }

    // Helper to reduce boilerplate for effects
    private fun sendSnackbar(messageRes: Int, action: (() -> Unit)? = null) {
        viewModelScope.launch(dispatcherIo) {
            _effects.send(
                UiEffect.ShowSnackbar(
                    messageRes = messageRes,
                    actionLabel = action?.let { RETRY },
                    action = action
                )
            )
        }
    }
}