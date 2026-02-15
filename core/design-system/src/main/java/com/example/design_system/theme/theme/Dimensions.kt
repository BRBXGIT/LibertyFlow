package com.example.design_system.theme.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class LibertyFlowAnimationTokens(
    val extraShort: Int = 100,
    val short: Int = 300,
    val medium: Int = 500,
    val long: Int = 700,
    val extraLong: Int = 1000
)

data class LibertyFlowDimensions(
    // Paddings
    val paddingExtraSmall: Dp = 4.dp,
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 20.dp,
    val paddingExtraLarge: Dp = 24.dp,

    // Spacers
    val spacingExtraSmall: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 20.dp,
    val spacingExtraLarge: Dp = 24.dp,
)

val LocalDimensions = staticCompositionLocalOf { LibertyFlowDimensions() }
val LocalAnimationTokens = staticCompositionLocalOf { LibertyFlowAnimationTokens() }