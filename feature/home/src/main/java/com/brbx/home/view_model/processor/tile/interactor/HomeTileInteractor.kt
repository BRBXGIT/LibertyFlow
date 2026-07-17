package com.brbx.home.view_model.processor.tile.interactor

import com.brbx.common.model.common.model.Tile

internal interface HomeTileInteractor {
    suspend fun getTile(): Tile?
}