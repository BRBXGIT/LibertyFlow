package com.example.design_system.components.bottom_nav_bar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun calculateNavBarSize(): Dp {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    return BottomNavBarUtils.BOTTOM_BAR_HEIGHT.dp + bottomPadding
}