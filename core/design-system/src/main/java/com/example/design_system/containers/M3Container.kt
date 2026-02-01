package com.example.design_system.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mShapes

private val HORIZONTAL_PADDING = 16.dp

@Composable
fun M3Container(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        content = content,
        modifier = modifier
            .padding(horizontal = HORIZONTAL_PADDING)
            .clip(mShapes.small)
            .background(mColors.surfaceContainer)
    )
}