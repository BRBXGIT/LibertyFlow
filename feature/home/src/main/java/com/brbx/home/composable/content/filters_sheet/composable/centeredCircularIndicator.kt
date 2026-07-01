package com.brbx.home.composable.content.filters_sheet.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.brbx.home.composable.content.filters_sheet.FiltersSheetKeys
import com.brbx.ui_compose.modifiers.brbxAnimateItem

internal fun LazyGridScope.centeredCircularIndicator() {
    item(
        key = FiltersSheetKeys.GenresIndicatorKey,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        IndicatorItem(
            modifier = Modifier
                .fillMaxWidth()
                .brbxAnimateItem(scope = this)
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun IndicatorItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ContainedLoadingIndicator()
    }
}