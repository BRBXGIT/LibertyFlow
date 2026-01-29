package com.example.design_system.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Animation timing constants
private const val ANIMATION_DURATION = 300
private const val OFFSET_DIVIDER = 2

@Composable
fun VerticalSlideAnimatedContainer(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    // Vertical slide + fade animation
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetY = { it / OFFSET_DIVIDER }
        ) + fadeIn(tween(ANIMATION_DURATION)),
        exit = slideOutVertically(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetY = { -it / OFFSET_DIVIDER }
        ) + fadeOut(tween(ANIMATION_DURATION))
    ) {
        content()
    }
}