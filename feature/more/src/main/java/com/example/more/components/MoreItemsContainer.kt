package com.example.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes

private const val HORIZONTAL_PADDING = 16

@Composable
fun LazyItemScope.MoreItemsContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        content = content,
        modifier = Modifier
            .animateItem()
            .padding(horizontal = HORIZONTAL_PADDING.dp)
            .clip(mShapes.small)
            .background(mColors.surfaceContainer)
    )
}