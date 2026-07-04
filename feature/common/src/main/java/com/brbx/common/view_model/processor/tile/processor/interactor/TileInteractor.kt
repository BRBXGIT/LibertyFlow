package com.brbx.common.view_model.processor.tile.processor.interactor

import com.brbx.common.view_model.processor.tile.model.CommonTile

internal interface TileInteractor {

    suspend fun getTile(): CommonTile
}