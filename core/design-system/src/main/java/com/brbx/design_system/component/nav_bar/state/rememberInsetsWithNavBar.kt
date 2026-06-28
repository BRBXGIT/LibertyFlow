package com.brbx.design_system.component.nav_bar.state

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberInsetsWithNavBar(): WindowInsets {
    val navBarHeight = rememberNavBarHeight()

    val bottomInset = remember(key1 = navBarHeight) { WindowInsets(bottom = navBarHeight) }
    return WindowInsets.systemBars.union(insets = bottomInset)
}