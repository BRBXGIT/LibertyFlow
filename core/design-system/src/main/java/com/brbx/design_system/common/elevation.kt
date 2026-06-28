package com.brbx.design_system.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
internal fun rememberElevationColor(): Color =
    remember { Color.Black.copy(alpha = 0.7f) }
