package com.brbx.home.view_model.processor.random_anime

import arrow.optics.copy
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.model.loading_state.isLoading
import com.brbx.common.strings.asRes
import com.brbx.common.view_model.postSnackbarEffect
import com.brbx.domain.network.model.result.onError
import com.brbx.domain.network.model.result.onSuccess
import com.brbx.domain.network.releases.random.use_case.GetRandomAnimeReleaseUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.loadingState
import com.brbx.home.view_model.model.randomAnime
import com.brbx.mvi.view_model.BrbxMviScope
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.snackbar.config.BrbxSnackbarDuration
import com.brbx.ui_compose.components.complex.snackbar.config.DefaultBrbxSnackbarConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class RandomAnimeProcessorImpl(
    private val randomAnimeUseCase: GetRandomAnimeReleaseUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : RandomAnimeProcessor {

    override fun BrbxMviScope<State, BrbxEffect, Unit>.process(intent: Intent.GetRandomAnime) {
        when (intent) {
            is Intent.GetRandomAnime ->
                getRandomAnime()
        }
    }

    private fun BrbxMviScope<State, BrbxEffect, Unit>.getRandomAnime() {
        coroutineScope.launch(context = dispatcherIo) {
            updateState { copy { State.randomAnime.loadingState.isLoading set true } }
            randomAnimeUseCase()
                .onSuccess {
                    // TODO Make navigation to details screen
                } onError { exception ->
                    postSnackbarEffect(
                        config = DefaultBrbxSnackbarConfig(
                            text = exception.asRes().toBrbxText(),
                            duration = BrbxSnackbarDuration.Infinite,
                            isDismissable = false,
                            buttonText = CommonStrings.retry.toBrbxText(),
                            onButtonClick = { getRandomAnime() },
                        )
                    )
                }
            updateState { copy { State.randomAnime.loadingState.isLoading set false } }
        }
    }
}