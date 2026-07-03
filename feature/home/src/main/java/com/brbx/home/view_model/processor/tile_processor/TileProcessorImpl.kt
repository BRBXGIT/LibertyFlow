package com.brbx.home.view_model.processor.tile_processor

import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.model.DomainLatestWatchingAnime
import com.brbx.domain.local_dbs.watching_anime.use_case.anime.use_case.GetLatestWatchingAnimeUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.state.State
import com.brbx.home.view_model.model.state.Tile
import com.brbx.ui_compose.common.toBrbxText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class TileProcessorImpl(
    private val latestWatchedAnimeUseCase: GetLatestWatchingAnimeUseCase,
    private val dispatcherIo: CoroutineDispatcher,
) : TileProcessor {

    override fun LibertyFlowMviScope<State>.process(intent: Intent.TileIntent) {
        when (intent) {
            is Intent.TileIntent.GetTile -> {
                coroutineScope.launch(context = dispatcherIo) {
                    val result = latestWatchedAnimeUseCase().toUi() ?: Tile.Theme()
                    updateState {
                        val visibility = tile?.isPrecollectionVisible ?: false
                        val updatedResult = when (result) {
                            is Tile.LatestWatching ->
                                result.copy(isPrecollectionVisible = visibility)

                            is Tile.Theme ->
                                result.copy(isPrecollectionVisible = visibility)
                        }
                        copy(tile = updatedResult)
                    }
                }
            }

            is Intent.TileIntent.TogglePrecollectionVisibility -> {
                updateState {
                    copy(
                        tile = tile?.let { currentTile ->
                            when (currentTile) {
                                is Tile.LatestWatching ->
                                    currentTile.copy(isPrecollectionVisible = !currentTile.isPrecollectionVisible)

                                is Tile.Theme ->
                                    currentTile.copy(isPrecollectionVisible = !currentTile.isPrecollectionVisible)
                            }
                        },
                    )
                }
            }
        }
    }

    private fun DomainLatestWatchingAnime?.toUi(): Tile.LatestWatching? =
        this?.let {
            Tile.LatestWatching(
                animeId = this.animeId,
                title = this.title.toBrbxText(),
            )
        }
}
