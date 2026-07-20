package com.brbx.home.view_model.processor.tile.interactor

import com.brbx.common.model.common.model.DefaultTile
import com.brbx.home.view_model.processor.tile.interactor.latest_watched.LatestWatchedTileInteractor
import com.brbx.home.view_model.processor.tile.interactor.stub.StubTileInteractor

internal class HomeTileInteractorImpl(
    private val latestWatchedTileInteractor: LatestWatchedTileInteractor,
    private val stubTileInteractor: StubTileInteractor,
) : HomeTileInteractor {

    override suspend fun getTile(): DefaultTile? {
        return latestWatchedTileInteractor.getTile()
            ?: stubTileInteractor.getTile()
    }
}
