package com.brbx.home.view_model.processor.tile.interactor

import com.brbx.common.model.common.model.DefaultTile

internal interface HomeTileInteractor {
    suspend fun getTile(): DefaultTile?
}