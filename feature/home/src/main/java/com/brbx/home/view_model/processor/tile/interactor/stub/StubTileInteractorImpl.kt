package com.brbx.home.view_model.processor.tile.interactor.stub

import com.brbx.common.model.common.model.DefaultTile
import com.brbx.home.view_model.processor.tile.interactor.tile_factory.HomeTileFactory
import kotlin.random.Random

internal class StubTileInteractorImpl(
    private val tileFactory: HomeTileFactory,
) : StubTileInteractor {

    override suspend fun getTile(): DefaultTile {
        return if (Random.nextInt(until = 10) < 9) {
            tileFactory.createThemeTile()
        } else {
            tileFactory.createAniLibertyTile()
        }
    }
}
