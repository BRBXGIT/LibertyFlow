package com.brbx.common.view_model.processor.tile.processor.interactor

import com.brbx.common.view_model.processor.tile.model.CommonTileState

internal interface TileInteractor {

    suspend fun getTile(): CommonTileState
}