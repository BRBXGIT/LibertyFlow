package com.example.design_system.theme.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data class representing the duration tokens for animations within the LibertyFlow system.
 * * These tokens provide a unified set of timing values to ensure consistent motion
 * across the application, ranging from quick micro-interactions to long transitions.
 *
 * @property extraShort 100ms - Used for small UI changes like checkbox toggles.
 * @property short 300ms - Standard for simple enter/exit transitions.
 * @property medium 500ms - Used for larger element movements or shared element transitions.
 * @property long 700ms - Suitable for complex layout changes or screen-level transitions.
 * @property extraLong 900ms - Used for slow-motion effects or emphasizing major state changes.
 */
data class LibertyFlowAnimationTokens(
    val extraShort: Int = 100,
    val short: Int = 300,
    val medium: Int = 500,
    val long: Int = 700,
    val extraLong: Int = 900
)

/**
 * Data class defining the layout and spacing scale for the LibertyFlow design system.
 * * This follows a consistent 4dp/8dp grid logic to maintain visual rhythm.
 * * @property paddingExtraSmall 4dp - Minimal internal padding.
 * @property paddingSmall 8dp - Small internal padding for buttons or cards.
 * @property paddingMedium 16dp - The standard padding for screen margins and large containers.
 * @property paddingLarge 20dp - Increased emphasis padding.
 * @property paddingExtraLarge 24dp - Maximum padding for spacious layouts.
 * @property spacingExtraSmall 4dp - Tight spacing between related icons/text.
 * @property spacingSmall 8dp - Standard spacing between elements in a list.
 * @property spacingMedium 16dp - Vertical spacing between logical sections.
 * @property spacingLarge 20dp - Large gaps for distinct UI blocks.
 * @property spacingExtraLarge 24dp - Maximum spacing for hero sections.
 */
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

/**
 * CompositionLocal used to provide [LibertyFlowDimensions] throughout the UI tree.
 * Uses [staticCompositionLocalOf] as these values are generally constant.
 */
val LocalDimensions = staticCompositionLocalOf { LibertyFlowDimensions() }

/**
 * CompositionLocal used to provide [LibertyFlowAnimationTokens] throughout the UI tree.
 * Uses [staticCompositionLocalOf] as these values are generally constant.
 */
val LocalAnimationTokens = staticCompositionLocalOf { LibertyFlowAnimationTokens() }