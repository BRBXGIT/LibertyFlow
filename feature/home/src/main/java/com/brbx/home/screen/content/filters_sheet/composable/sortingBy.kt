package com.brbx.home.screen.content.filters_sheet.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.brbx.domain.network.model.common.Sorting
import com.brbx.home.common.HomeStrings
import com.brbx.home.view_model.model.Intent
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.mTypography

private val sortings = listOf(Sorting.RatingDesc, Sorting.CreatedAtDesc)

internal fun LazyGridScope.sortingBy(
    selected: Sorting,
    dispatchIntent: (Intent) -> Unit
) {
    items(
        items = sortings,
        key = { sorting -> sorting },
        span = { GridItemSpan(currentLineSpan = maxLineSpan) }
    ) { sorting ->
        SortingItem(
            modifier = Modifier.brbxAnimateItem(scope = this),
            sorting = sorting,
            selected = selected,
            dispatchIntent = dispatchIntent,
        )
    }
}

@Composable
private fun SortingItem(
    modifier: Modifier = Modifier,
    sorting: Sorting,
    selected: Sorting,
    dispatchIntent: (Intent) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(bDimens.micro4)
    ) {
        RadioButton(
            selected = sorting == selected,
            onClick = { dispatchIntent(Intent.Filters.UpdateSorting(sorting)) }
        )

        Text(
            text = stringResource(id = sorting.toStringRes()),
            style = mTypography.bodyLarge
        )
    }
}

private fun Sorting.toStringRes(): Int =
    when (this) {
        Sorting.RatingDesc -> HomeStrings.filters_sheet_sorting_by_popularity
        else -> HomeStrings.filters_sheet_sorting_by_novelty
    }