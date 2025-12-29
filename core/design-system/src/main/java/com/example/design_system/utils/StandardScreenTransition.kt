package com.example.design_system.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

private const val ANIMATION_DURATION = 300

fun standardScreenEnterTransition() = fadeIn(tween(ANIMATION_DURATION))

fun standardScreenExitTransition() = fadeOut(tween(ANIMATION_DURATION))