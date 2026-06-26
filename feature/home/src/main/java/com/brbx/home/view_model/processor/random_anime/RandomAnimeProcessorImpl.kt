package com.brbx.home.view_model.processor.random_anime

import arrow.optics.copy
import com.brbx.common.model.loading_state.isLoading
import com.brbx.common.strings.asBrbxText
import com.brbx.common.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.postNetworkExceptionSnackbar
import com.brbx.domain.network.model.result.onError
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.releases.random.use_case.GetRandomAnimeReleaseUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.loadingState
import com.brbx.home.view_model.model.randomAnime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class RandomAnimeProcessorImpl(
    private val randomAnimeUseCase: GetRandomAnimeReleaseUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : RandomAnimeProcessor {

    override fun LibertyFlowMviScope<State>.process(intent: Intent.GetRandomAnime) {
        when (intent) {
            is Intent.GetRandomAnime ->
                getRandomAnime()
        }
    }

    private fun LibertyFlowMviScope<State>.getRandomAnime() {
        coroutineScope.launch(context = dispatcherIo) {
            updateState { copy { State.randomAnime.loadingState.isLoading set true } }
            randomAnimeUseCase()
                .onSuccess {
                    // TODO Make navigation to details screen
                } onError { exception ->
                    postNetworkExceptionSnackbar(exception.asBrbxText()) { getRandomAnime() }
                }
            updateState { copy { State.randomAnime.loadingState.isLoading set false } }
        }
    }
}