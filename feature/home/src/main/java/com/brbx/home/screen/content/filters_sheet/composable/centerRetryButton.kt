package com.brbx.home.screen.content.filters_sheet.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.brbx.common.model.alias.CommonStrings
import com.brbx.home.screen.content.filters_sheet.FiltersSheetKeys
import com.brbx.home.view_model.model.Intent
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.mTypography

internal fun LazyGridScope.centeredRetryButton(
    dispatchIntent: (Intent) -> Unit,
) {
    item(
        key = FiltersSheetKeys.GenresIndicatorKey,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        RetryButtonItem(
            dispatchIntent = dispatchIntent,
            modifier = Modifier
                .fillMaxWidth()
                .brbxAnimateItem(scope = this),
        )
    }
}

@Composable
private fun RetryButtonItem(
    dispatchIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { dispatchIntent(Intent.Filters.LoadGenres) }
        ) {
            Text(
                text = stringResource(id = CommonStrings.retry),
                style = mTypography.bodyMedium,
            )
        }
    }
}