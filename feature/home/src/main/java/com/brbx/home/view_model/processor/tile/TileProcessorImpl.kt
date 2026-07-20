package com.brbx.home.view_model.processor.tile

import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.processor.tile.interactor.HomeTileInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class TileProcessorImpl(
    private val homeTileInteractor: HomeTileInteractor,
    private val dispatcherIo: CoroutineDispatcher,
) : TileProcessor {

    override fun LibertyFlowMviScope<HomeState>.process(intent: HomeIntent.Tile) {
        when (intent) {
            HomeIntent.Tile.GetTile -> loadTile()
            HomeIntent.Tile.TogglePrecollectionVisibility -> togglePrecollectionVisibility()
        }
    }

    private fun LibertyFlowMviScope<HomeState>.loadTile() {
        coroutineScope.launch(context = dispatcherIo) {
            val tile = homeTileInteractor.getTile()

            updateState {
                copy(tile = tile)
            }
        }
    }

    private fun LibertyFlowMviScope<HomeState>.togglePrecollectionVisibility() {
        updateState {
            copy(
                tile = tile?.copy(
                    isPrecollectionVisible = !tile.isPrecollectionVisible,
                ),
            )
        }
    }
}
