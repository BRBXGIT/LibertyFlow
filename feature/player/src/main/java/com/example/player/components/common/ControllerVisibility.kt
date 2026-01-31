@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.design_system.theme.mMotionScheme
import androidx.compose.runtime.getValue

private const val FULL_ALPHA = 1f
private const val GHOST_ALPHA = 0f
private const val OVERLAY_TINT_ALPHA = 0.5f

internal class ControllerVisibility(
    val controlsAlpha: Float,
    val overlayAlpha: Float
)

@Composable
internal fun rememberControllerVisibility(
    isVisible: Boolean,
    isScrubbing: Boolean = false,
): ControllerVisibility {
    val motion = mMotionScheme

    val controlsAlpha by animateFloatAsState(
        targetValue = if (isVisible || isScrubbing) FULL_ALPHA else GHOST_ALPHA,
        animationSpec = motion.slowEffectsSpec(),
        label = "ControlsAlpha"
    )

    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible || isScrubbing) OVERLAY_TINT_ALPHA else GHOST_ALPHA,
        animationSpec = motion.slowEffectsSpec(),
        label = "OverlayAlpha"
    )

    return remember(controlsAlpha, overlayAlpha) {
        ControllerVisibility(controlsAlpha, overlayAlpha)
    }
}