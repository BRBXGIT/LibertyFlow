package com.brbx.common.view_model.processor.tile.processor.processor

import arrow.optics.Lens
import com.brbx.common.view_model.processor.tile.model.CommonTileIntent
import com.brbx.common.view_model.processor.tile.model.CommonTileState
import com.brbx.common.view_model.processor.tile.model.TileType
import com.brbx.common.view_model.processor.tile.processor.interactor.episode.EpisodeTileInteractor
import com.brbx.common.view_model.processor.tile.processor.interactor.recommendation.RecommendationTileInteractor
import com.brbx.common.view_model.processor.tile.processor.interactor.stub.StubTileInteractor
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class CommonTileProcessorImpl<State>(
    private val episodeInteractor: EpisodeTileInteractor,
    private val recommendationInteractor: RecommendationTileInteractor,
    private val stubInteractor: StubTileInteractor,
    private val tileLens: Lens<State, CommonTileState?>,
    private val dispatcherIo: CoroutineDispatcher,
) : CommonTileProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonTileIntent) {
        when (intent) {
            is CommonTileIntent.GetTile -> {
                coroutineScope.launch(context = dispatcherIo) {
                    val tile = when (intent.type) {
                        is TileType.Episode -> episodeInteractor.getTile()
                        is TileType.Recommendation -> recommendationInteractor.getTile()
                        is TileType.Stub -> stubInteractor.getTile()
                    }
                    updateState {
                        tileLens.set(source = this, focus = tile)
                    }
                }
            }
            CommonTileIntent.TogglePrecollectionVisibility -> {
                updateState {
                    tileLens.modify(source = this) {
                        it?.copy(isPrecollectionVisible = !it.isPrecollectionVisible)
                    }
                }
            }
        }
    }
}