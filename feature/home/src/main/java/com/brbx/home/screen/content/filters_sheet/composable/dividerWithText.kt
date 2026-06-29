package com.brbx.home.screen.content.filters_sheet.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import com.brbx.design_system.component.divider.DividerWithText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.modifiers.brbxAnimateItem

internal fun LazyGridScope.filterDivider(@StringRes text: Int) {
    item(
        key = text,
        span = { GridItemSpan(currentLineSpan = maxLineSpan) }
    ) {
        DividerWithText(
            modifier = Modifier
                .fillMaxWidth()
                .brbxAnimateItem(scope = this),
            text = text.toBrbxText(),
        )
    }
}