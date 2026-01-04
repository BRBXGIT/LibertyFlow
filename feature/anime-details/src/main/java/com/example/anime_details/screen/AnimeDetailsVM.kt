package com.example.anime_details.screen

import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.UiEffect
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.toWhileSubscribed
import com.example.data.domain.AuthRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.domain.WatchedEpsRepo
import com.example.data.models.auth.AuthState
import com.example.data.models.auth.UiTokenRequest
import com.example.data.models.favorites.UiFavoriteItem
import com.example.data.models.favorites.UiFavoriteRequest
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

@HiltViewModel
class AnimeDetailsVM @Inject constructor(
    authRepo: AuthRepo,
    private val releasesRepo: ReleasesRepo,
    private val watchedEpsRepo: WatchedEpsRepo,
    private val favoritesRepo: FavoritesRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): BaseAuthVM(authRepo, dispatcherIo) {

    private val _animeDetailsState = MutableStateFlow(AnimeDetailsState())
    val animeDetailsState = _animeDetailsState.toWhileSubscribed(AnimeDetailsState())

    private val _animeDetailsEffects = Channel<UiEffect>(Channel.BUFFERED)
    val animeDetailsEffects = _animeDetailsEffects.receiveAsFlow()

    // Main data
    private fun fetchAnime(id: Int) {
        viewModelScope.launch(dispatcherIo) {
            _animeDetailsState.update { it.onDataQueryStart() }

            releasesRepo.getAnime(id)
                .onSuccess { uiAnimeDetails ->
                    _animeDetailsState.update { it.copy(anime = uiAnimeDetails) }
                }
                .onError { _, messageRes ->
                    _animeDetailsState.update { it.onDataQueryError() }

                    sendEffect(
                        UiEffect.ShowSnackbar(
                            messageRes = messageRes,
                            actionLabel = "Retry",
                            action = { fetchAnime(id) }
                        )
                    )
                }

            _animeDetailsState.update { it.onDataQueryEnd() }
        }
    }

    // Episodes
    private fun observeWatchedEpisodes(animeId: Int) {
        viewModelScope.launch(dispatcherIo) {
            watchedEpsRepo.insertTitle(animeId)
            watchedEpsRepo.getWatchedEpisodes(animeId).collect { episodes ->
                _animeDetailsState.update { it.setWatchedEps(episodes) }
            }
        }
    }

    private fun addEpisodeToWatched(episodeIndex: Int) {
        viewModelScope.launch(dispatcherIo) {
            watchedEpsRepo.insertWatchedEpisode(
                animeId = _animeDetailsState.value.anime!!.id,
                episodeIndex = episodeIndex
            )
        }
    }

    // Favorites
    private fun fetchFavoritesIds() {
        viewModelScope.launch(dispatcherIo) {
            _animeDetailsState.update { it.onFavoritesQueryStart() }

            favoritesRepo.getFavoritesIds()
                .onSuccess { ids ->
                    _animeDetailsState.update { it.copy(favoritesIds = ids) }
                }
                .onError { _, messageRes ->
                    _animeDetailsState.update { it.onFavoritesQueryError() }

                    sendEffect(
                        UiEffect.ShowSnackbar(
                            messageRes = messageRes,
                            actionLabel = "Retry",
                            action = { fetchFavoritesIds() }
                        )
                    )
                }

            _animeDetailsState.update { it.onFavoritesQueryEnd() }
        }
    }

    private fun addToFavorites() {
        viewModelScope.launch(dispatcherIo) {
            _animeDetailsState.update { it.onFavoritesQueryStart() }

            val request = UiFavoriteRequest().apply {
                add(UiFavoriteItem(_animeDetailsState.value.anime!!.id))
            }
            delay(2000) // Just cause i want to show animation :)
            favoritesRepo.addFavorite(request)
                .onSuccess {
                    _animeDetailsState.update { it.addAnimeToFavorites() }
                }
                .onError { _, messageRes ->
                    _animeDetailsState.update { it.onFavoritesQueryError() }

                    sendEffect(
                        UiEffect.ShowSnackbar(
                            messageRes = messageRes,
                            actionLabel = "Retry",
                            action = { addToFavorites() }
                        )
                    )
                }

            _animeDetailsState.update { it.onFavoritesQueryEnd() }
        }
    }

    private fun removeFromFavorites() {
        viewModelScope.launch(dispatcherIo) {
            _animeDetailsState.update { it.onFavoritesQueryStart() }

            val request = UiFavoriteRequest().apply {
                add(UiFavoriteItem(_animeDetailsState.value.anime!!.id))
            }
            delay(2000) // Just cause i want to show animation :)
            favoritesRepo.deleteFavorite(request)
                .onSuccess {
                    _animeDetailsState.update { it.removeAnimeFromFavorites() }
                }
                .onError { _, messageRes ->
                    _animeDetailsState.update { it.onFavoritesQueryError() }

                    sendEffect(
                        UiEffect.ShowSnackbar(
                            messageRes = messageRes,
                            actionLabel = "Retry",
                            action = { removeFromFavorites() }
                        )
                    )
                }

            _animeDetailsState.update { it.onFavoritesQueryEnd() }
        }
    }

    fun sendEffect(effect: UiEffect) {
        viewModelScope.launch(dispatcherIo) {
            _animeDetailsEffects.send(effect)
        }
    }

    fun sendIntent(intent: AnimeDetailsIntent) {
        when(intent) {
            // Data
            is AnimeDetailsIntent.FetchAnime ->
                fetchAnime(intent.id)
            is AnimeDetailsIntent.ObserveWatchedEps ->
                observeWatchedEpisodes(intent.id)
            is AnimeDetailsIntent.AddEpisodeToWatched ->
                addEpisodeToWatched(intent.episodeIndex)
            AnimeDetailsIntent.GetTokens -> {
                getAuthToken(
                    request = UiTokenRequest(_animeDetailsState.value.email, _animeDetailsState.value.password),
                    onStart = { _animeDetailsState.update { it.setIsPasswordOrEmailIncorrect(false) } },
                    onIncorrectData = { _animeDetailsState.update { it.setIsPasswordOrEmailIncorrect(true) } },
                    onAnyError = { messageRes, retry ->
                        sendEffect(
                            effect = UiEffect.ShowSnackbar(
                                messageRes = messageRes,
                                actionLabel = "Retry",
                                action = retry
                            )
                        )
                    }
                )
            }

            // Favorites
            AnimeDetailsIntent.AddToFavorite -> addToFavorites()
            AnimeDetailsIntent.RemoveFromFavorite -> removeFromFavorites()

            // Toggles
            AnimeDetailsIntent.ToggleIsDescriptionExpanded ->
                _animeDetailsState.update { it.toggleIsDescriptionExpanded() }
            AnimeDetailsIntent.ToggleIsAuthBsVisible ->
                _animeDetailsState.update { it.toggleAuthBSVisible() }

            // Updates
            is AnimeDetailsIntent.UpdateEmail ->
                _animeDetailsState.update { it.updateEmail(intent.email) }
            is AnimeDetailsIntent.UpdatePassword ->
                _animeDetailsState.update { it.updatePassword(intent.password) }
        }
    }

    init {
        observeAuthState { authState ->
            _animeDetailsState.update { it.setAuthState(authState) }
            if (authState is AuthState.LoggedIn) fetchFavoritesIds()
        }
    }
}