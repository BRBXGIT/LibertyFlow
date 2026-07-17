package com.brbx.common.model.common.model

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import com.brbx.common.model.common.model.Tile.Precollection
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText

@Immutable
@optics
data class DefaultTile(
    override val title: BrbxText,
    override val description: BrbxText,
    override val icon: BrbxIcon,
    override val precollection: Precollection? = null,
    override val isPrecollectionVisible: Boolean = false,
) : Tile { companion object }