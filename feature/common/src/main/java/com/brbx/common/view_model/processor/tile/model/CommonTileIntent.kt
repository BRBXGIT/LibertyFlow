package com.brbx.common.view_model.processor.tile.model

sealed interface CommonTileIntent {

    @JvmInline value class GetTile(val type: TileType) : CommonTileIntent

    data object TogglePrecollectionVisibility : CommonTileIntent
}