package com.brbx.home.view_model.processor.tile

import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.processor.tile.interactor.latest_watched.LatestWatchedTileInteractor
import com.brbx.home.view_model.processor.tile.interactor.stub.StubTileInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class TileProcessorImpl(
    private val stubInteractor: StubTileInteractor,
    private val latestWatchedTileInteractor: LatestWatchedTileInteractor,
    private val dispatcherIo: CoroutineDispatcher,
) : TileProcessor {

    override fun LibertyFlowMviScope<HomeState>.process(intent: HomeIntent.Tile) {
        when (intent) {
            is HomeIntent.Tile.GetTile -> {
                coroutineScope.launch(context = dispatcherIo) {
                    val tile = latestWatchedTileInteractor.getTile() ?:
                        stubInteractor.getTile()
                    updateState { copy(tile = tile) }
                }
            }
            is HomeIntent.Tile.TogglePrecollectionVisibility -> {
                updateState {
                    copy(tile = this.tile?.copy(isPrecollectionVisible = !this.tile.isPrecollectionVisible))
                }
            }
        }
    }
}
