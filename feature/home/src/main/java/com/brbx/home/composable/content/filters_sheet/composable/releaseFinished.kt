package com.brbx.home.composable.content.filters_sheet.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.brbx.home.common.HomeStrings
import com.brbx.home.composable.content.filters_sheet.FiltersSheetKeys
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.mTypography

internal fun LazyGridScope.releaseFinished(
    isOngoing: Boolean,
    dispatchIntent: (HomeIntent) -> Unit
) {
    item(
        key = FiltersSheetKeys.ReleaseFinishedKey,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        ReleaseFinishedItem(
            dispatchIntent = dispatchIntent,
            isOngoing = isOngoing,
            modifier = Modifier.brbxAnimateItem(scope = this),
        )
    }
}

@Composable
private fun ReleaseFinishedItem(
    dispatchIntent: (HomeIntent) -> Unit,
    isOngoing: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(bDimens.micro4)
    ) {
        Checkbox(
            checked = isOngoing,
            onCheckedChange = { dispatchIntent(HomeIntent.Filters.ToggleOngoing) },
        )

        Text(
            text = stringResource(id = HomeStrings.filters_sheet_ongoing),
            style = mTypography.bodyLarge
        )
    }
}