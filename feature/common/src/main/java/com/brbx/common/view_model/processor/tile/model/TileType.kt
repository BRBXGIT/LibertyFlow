package com.brbx.common.view_model.processor.tile.model

sealed interface TileType {

    sealed interface Episode : TileType {
        data object LatestWatched : Episode
    }

    sealed interface Stub : TileType {
        data object Theme : Stub
    }
}