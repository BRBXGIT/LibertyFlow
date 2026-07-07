package com.brbx.home.composable.content.filters_sheet.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.brbx.common.model.common.model.Years
import com.brbx.home.common.HomeStrings
import com.brbx.home.composable.content.filters_sheet.FiltersSheetKeys
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.ui_compose.modifiers.brbxAnimateItem

internal fun LazyGridScope.yearFields(
    years: Years,
    dispatchIntent: (HomeIntent) -> Unit,
) {
    item(
        key = FiltersSheetKeys.FromYearKey,
        span = { GridItemSpan(currentLineSpan = maxLineSpan) },
    ) {
        YearField(
            labelRes = HomeStrings.filters_sheet_from_year,
            currentValue = years.from,
            onValueChanged = { dispatchIntent(HomeIntent.Filters.UpdateYears(from = it, to = years.to)) },
            modifier = Modifier
                .fillMaxWidth()
                .brbxAnimateItem(scope = this),
        )
    }

    item(
        key = FiltersSheetKeys.ToYearKey,
        span = { GridItemSpan(currentLineSpan = maxLineSpan) },
    ) {
        YearField(
            labelRes = HomeStrings.filters_sheet_to_year,
            currentValue = years.to,
            onValueChanged = { dispatchIntent(HomeIntent.Filters.UpdateYears(from = years.from, to = it)) },
            modifier = Modifier
                .fillMaxWidth()
                .brbxAnimateItem(scope = this),
        )
    }
}

@Composable
private fun YearField(
    @StringRes labelRes: Int,
    currentValue: Int,
    onValueChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = currentValue.toString(),
        onValueChange = { new ->
            new.toIntOrNull()?.let { onValueChanged(it) }
        },
        maxLines = 1,
        modifier = modifier,
        label = {
            Text(
                text = stringResource(id = labelRes)
            )
        }
    )
}