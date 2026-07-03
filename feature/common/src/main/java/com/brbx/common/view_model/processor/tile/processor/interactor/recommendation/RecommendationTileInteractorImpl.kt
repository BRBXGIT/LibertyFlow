package com.brbx.common.view_model.processor.tile.processor.interactor.recommendation

import com.brbx.common.view_model.processor.tile.model.CommonTileState
import com.brbx.common.view_model.processor.tile.processor.interactor.stub.StubTileInteractor

internal class RecommendationTileInteractorImpl(
    private val stubInteractor: StubTileInteractor,
) : RecommendationTileInteractor {

    // TODO Add recommendations tile
    override suspend fun getTile(): CommonTileState {
        return stubInteractor.getTile()
    }
}