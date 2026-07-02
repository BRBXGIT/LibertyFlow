package com.brbx.home.view_model.processor.random_anime

import com.brbx.common.strings.toBrbxText
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.common.view_model.view_model.postNetworkExceptionSnackbar
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.releases.random.use_case.GetRandomAnimeReleaseUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
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
                    makeNetworkCall(
                        loadingLens = State.randomAnime,
                        call = { randomAnimeUseCase() },
                    ).onSuccess {
                        // TODO Make navigation to details screen
                    } onException { exception ->
                        postNetworkExceptionSnackbar(exception.toBrbxText()) {
                            process(Intent.GetRandomAnime)
                        }
                    }
                }
            }
        }
    }
}