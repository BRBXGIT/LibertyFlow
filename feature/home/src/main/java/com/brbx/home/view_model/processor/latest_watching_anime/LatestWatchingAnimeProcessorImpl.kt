package com.brbx.home.view_model.processor.latest_watching_anime

import arrow.optics.copy
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.model.DomainLatestWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.GetLatestWatchingAnimeUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.latestWatchingAnime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class LatestWatchingAnimeProcessorImpl(
    private val latestWatchedAnimeUseCase: GetLatestWatchingAnimeUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : LatestWatchingAnimeProcessor {

    override fun LibertyFlowMviScope<State>.process(intent: Intent.GetLatestWatchingAnime) {
        when (intent) {
            is Intent.GetLatestWatchingAnime -> {
                coroutineScope.launch(context = dispatcherIo) {
                    val result = latestWatchedAnimeUseCase()?.toUi()
                    updateState { copy { State.latestWatchingAnime set result } }
                }
            }
        }
    }

    private fun DomainLatestWatchingAnime.toUi(): State.LatestWatchingAnime =
        State.LatestWatchingAnime(
            animeId = this.animeId,
            title = this.title,
            lastEpisode = this.lastEpisodeIndex + 1,
        )
}