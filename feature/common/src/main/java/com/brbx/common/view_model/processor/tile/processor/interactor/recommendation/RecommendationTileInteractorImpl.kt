package com.brbx.common.view_model.processor.tile.processor.interactor.recommendation

import com.brbx.common.view_model.processor.tile.model.CommonTile
import com.brbx.common.view_model.processor.tile.processor.interactor.stub.StubTileInteractor

internal class RecommendationTileInteractorImpl(
    private val stubInteractor: StubTileInteractor,
) : RecommendationTileInteractor {

    // TODO Add recommendations tile
    override suspend fun getTile(): CommonTile {
        return stubInteractor.getTile()
    }
}