package com.brbx.home.view_model.processor.tile

import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.processor.tile.interactor.HomeTileInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class TileProcessorImpl(
    private val homeTileInteractor: HomeTileInteractor,
    private val dispatcherIo: CoroutineDispatcher,
) : LibertyFlowIntentProcessor<HomeState, HomeIntent.Tile>(),
    TileProcessor {

    override fun process(intent: HomeIntent.Tile) {
        when (intent) {
            HomeIntent.Tile.GetTile -> loadTile()
            HomeIntent.Tile.TogglePrecollectionVisibility -> togglePrecollectionVisibility()
        }
    }

    private fun loadTile() {
        coroutineScope.launch(context = dispatcherIo) {
            val tile = homeTileInteractor.getTile()
            updateState {
                copy(tile = tile)
            }
        }
    }

    private fun togglePrecollectionVisibility() {
        updateState {
            copy(
                tile = tile?.copy(
                    isPrecollectionVisible = !tile.isPrecollectionVisible,
                ),
            )
        }
    }
}
