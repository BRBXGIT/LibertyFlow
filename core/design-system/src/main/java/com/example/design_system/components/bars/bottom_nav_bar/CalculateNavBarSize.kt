package com.example.design_system.components.bars.bottom_nav_bar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Calculates the total height of the navigation bar, factoring in the system
 * navigation bar insets to ensure content is not obscured by gesture or button bars.
 *
 * @return The total height in [Dp] including system padding.
 */
@Composable
fun calculateNavBarSize(): Dp {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    return BottomNavBarConstants.BOTTOM_BAR_HEIGHT.dp + bottomPadding
}