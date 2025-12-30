package com.example.anime_details.screen

import androidx.lifecycle.viewModelScope
import com.example.common.dispatchers.Dispatcher
import com.example.common.dispatchers.LibertyFlowDispatcher
import com.example.common.ui_helpers.UiEffect
import com.example.common.vm_helpers.BaseAuthVM
import com.example.common.vm_helpers.toWhileSubscribed
import com.example.data.domain.AuthRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.utils.remote.network_request.onError
import com.example.data.utils.remote.network_request.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsVM @Inject constructor(
    authRepo: AuthRepo,
    private val releasesRepo: ReleasesRepo,
    @param:Dispatcher(LibertyFlowDispatcher.IO) private val dispatcherIo: CoroutineDispatcher
): BaseAuthVM(authRepo, dispatcherIo) {

    private val _animeDetailsState = MutableStateFlow(AnimeDetailsState())
    val animeDetailsState = _animeDetailsState.toWhileSubscribed(AnimeDetailsState())

    private val _animeDetailsEffects = Channel<UiEffect>(Channel.BUFFERED)
    val animeDetailsEffects = _animeDetailsEffects.receiveAsFlow()

    private fun fetchAnime(id: Int) {
        viewModelScope.launch(dispatcherIo) {
            _animeDetailsState.update { it.copy(isLoading = true, isError = false) }

            releasesRepo.getAnime(id)
                .onSuccess { uiAnimeDetails ->
                    _animeDetailsState.update { it.copy(isLoading = false, anime = uiAnimeDetails) }
                }
                .onError { _, messageRes ->
                    _animeDetailsState.update { it.copy(isLoading = false, isError = true) }

                    sendEffect(
                        UiEffect.ShowSnackbar(
                            messageRes = messageRes,
                            actionLabel = "Retry",
                            action = { fetchAnime(id) }
                        )
                    )
                }
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
            is AnimeDetailsIntent.FetchAnime -> fetchAnime(intent.id)

            // Toggles
            AnimeDetailsIntent.ToggleIsDescriptionExpanded ->
                _animeDetailsState.update { it.toggleIsDescriptionExpanded() }
        }
    }

    init {
        observeAuthState { authState ->
            _animeDetailsState.update { it.setAuthState(authState) }
        }
    }
}