package com.brbx.home.view_model.processor.random_anime

import arrow.optics.copy
import com.brbx.common.strings.asBrbxText
import com.brbx.common.view_model.model.state.isLoading
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.postNetworkExceptionSnackbar
import com.brbx.domain.network.model.result.onError
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.releases.random.use_case.GetRandomAnimeReleaseUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.commonLoadingState
import com.brbx.home.view_model.model.randomAnime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class RandomAnimeProcessorImpl(
    private val randomAnimeUseCase: GetRandomAnimeReleaseUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : RandomAnimeProcessor {

    override fun LibertyFlowMviScope<State>.process(intent: Intent.GetRandomAnime) {
        when (intent) {
            is Intent.GetRandomAnime -> {
                coroutineScope.launch(context = dispatcherIo) {
                    updateState { copy { State.randomAnime.commonLoadingState.isLoading set true } }
                    randomAnimeUseCase()
                        .onSuccess {
                            // TODO Make navigation to details screen
                        } onError { exception ->
                            postNetworkExceptionSnackbar(exception.asBrbxText()) { process(intent) }
                        }
                    updateState { copy { State.randomAnime.commonLoadingState.isLoading set false } }
                }
            }
        }
    }
}