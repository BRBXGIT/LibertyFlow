package com.brbx.design_system.component.bar.nav_bar.state

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.brbx.design_system.component.bar.nav_bar.NavBarConstants

@Composable
internal fun rememberNavBarHeight(): Dp {
    val systemNavBarPadding =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    return remember(key1 = systemNavBarPadding) { NavBarConstants.Height + systemNavBarPadding }
}