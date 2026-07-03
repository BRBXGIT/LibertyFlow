package com.brbx.common.view_model.processor.tile.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText

@Immutable
@optics
data class CommonTileState(
    val type: TileType,
    val title: BrbxText,
    val description: BrbxText,
    val icon: BrbxIcon,
    val precollection: Precollection?,
    val isPrecollectionVisible: Boolean,
) {
    @Immutable
    @optics
    data class Precollection(
        val label: BrbxText,
        val icon: BrbxIcon?,
    ) { companion object }

    companion object
}