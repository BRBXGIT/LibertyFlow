package com.brbx.home.view_model.processor.random_anime

import com.brbx.common.model.common.map.toBrbxText
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.common.view_model.view_model.makeNetworkCall
import com.brbx.common.view_model.view_model.postExceptionSnackbar
import com.brbx.domain.network.model.result.onException
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.releases.random.use_case.GetRandomAnimeReleaseUseCase
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.model.randomAnime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class RandomAnimeProcessorImpl(
    private val randomAnimeUseCase: GetRandomAnimeReleaseUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : RandomAnimeProcessor {

    override fun LibertyFlowMviScope<HomeState>.process(homeIntent: HomeIntent.GetRandomAnime) {
        when (homeIntent) {
            is HomeIntent.GetRandomAnime -> {
                coroutineScope.launch(context = dispatcherIo) {
                    delay(duration = 2000.milliseconds) // Used for animation but Antipattern
                    makeNetworkCall(
                        loadingLens = HomeState.randomAnime,
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