package com.example.design_system.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

/**
 * The default duration for screen-level transitions in milliseconds.
 * Set to 300ms to balance perceived speed with visual smoothness.
 */
private const val ANIMATION_DURATION = 300

/**
 * Defines a standard enter transition for new screens.
 * Uses a simple [fadeIn] effect with a [tween] animation for a clean,
 * non-distracting arrival.
 * @return An EnterTransition suitable for use in a NavGraph.
 */
fun standardScreenEnterTransition() = fadeIn(tween(ANIMATION_DURATION))

/**
 * Defines a standard exit transition for screens leaving the viewport.
 * Uses a [fadeOut] effect to softly transition the current screen out
 * as the new content takes focus.
 * @return An ExitTransition suitable for use in a NavGraph.
 */
fun standardScreenExitTransition() = fadeOut(tween(ANIMATION_DURATION))