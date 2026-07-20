package com.brbx.common.composable.utils

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import com.brbx.common.model.common.model.Tile
import com.brbx.design_system.component.card.tile.Tile
import com.brbx.ui_compose.modifiers.brbxAnimateItem

fun LazyGridScope.tileItem(
    tile: Tile,
    onTileClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPrecollectionClick: (() -> Unit)? = null,
    span: LazyGridItemSpanScope.() -> GridItemSpan = { GridItemSpan(currentLineSpan = maxLineSpan) },
    key: Any? = null
) {
    item(
        key = key,
        span = span,
        contentType = "Tile",
    ) {
        Tile(
            title = tile.title,
            description = tile.description,
            icon = tile.icon,
            onTileClick = onTileClick,
            modifier = modifier.brbxAnimateItem(scope = this),
            isPrecollectionVisible = tile.isPrecollectionVisible,
            precollectionIcon = tile.precollection?.icon,
            precollectionText = tile.precollection?.label,
            onPrecollectionClick = onPrecollectionClick ?: onTileClick,
        )
    }
}