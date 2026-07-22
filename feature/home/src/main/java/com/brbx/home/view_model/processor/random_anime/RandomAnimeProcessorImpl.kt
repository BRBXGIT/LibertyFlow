package com.brbx.home.view_model.processor.random_anime

import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.view_model.view_model.processor.LibertyFlowIntentProcessor
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.releases.random.use_case.GetRandomAnimeReleaseUseCase
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.model.randomAnime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class RandomAnimeProcessorImpl(
    private val randomAnimeUseCase: GetRandomAnimeReleaseUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : LibertyFlowIntentProcessor<HomeState, HomeIntent.GetRandomAnime>(),
    RandomAnimeProcessor {

    override fun process(intent: HomeIntent.GetRandomAnime) {
        when (intent) {
            is HomeIntent.GetRandomAnime -> {
                coroutineScope.launch(context = dispatcherIo) {
                    makeNetworkCall(
                        loadingLens = HomeState.randomAnime,
                        callDelay = 2_000L,
                        call = { randomAnimeUseCase() },
                    ).onSuccess {
                        // TODO Make navigation to details screen
                    } onException { exception ->
                        postExceptionSnackbar(exception.toBrbxText()) {
                            process(HomeIntent.GetRandomAnime)
                        }
                    }
                }
            }
        }
    }
}