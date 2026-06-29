package com.brbx.home.screen.content.filters_sheet.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.asString
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.mTypography

internal fun <T> LazyGridScope.selectableFilterItems(
    items: List<T>,
    isSelected: (T) -> Boolean,
    itemText: @Composable (T) -> BrbxText,
    onItemClick: (item: T) -> Unit,
    itemKey: (T) -> Any,
) {
    items(
        items = items,
        key = itemKey,
    ) { item ->
        val selected = isSelected(item)

        FilterChip(
            modifier = Modifier.brbxAnimateItem(scope = this),
            selected = selected,
            onClick = { onItemClick(item) },
            label = {
                Text(
                    text = itemText(item).asString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = mTypography.bodyMedium,
                    modifier = Modifier.padding(all = bDimens.micro5),
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}