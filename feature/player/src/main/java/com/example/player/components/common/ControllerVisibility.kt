@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.example.design_system.theme.theme.mMotionScheme
import androidx.compose.runtime.getValue

private const val FULL_ALPHA = 1f
private const val GHOST_ALPHA = 0f
private const val OVERLAY_TINT_ALPHA = 0.5f

/**
 * Represents the opacity levels for the player's control interface.
 * @property controlsAlpha The opacity of the interactive elements (buttons, seekbar, labels).
 * @property overlayAlpha The opacity of the background scrim/tint that improves contrast
 * when controls are visible.
 */
@Immutable
internal data class ControllerVisibility(
    val controlsAlpha: Float,
    val overlayAlpha: Float
)

/**
 * A state-helper that calculates and animates the visibility of the player controls.
 *
 * It uses a coordinated animation to fade the UI in and out. The controls remain
 * fully visible if the user is currently scrubbing through the video, regardless
 * of the standard visibility timer.
 *
 * @param isVisible The primary visibility state, usually controlled by a timer or user tap.
 * @param isScrubbing If true, forces the controls to remain visible while the user
 * interacts with the seekbar.
 * @return A [ControllerVisibility] object containing the current animated alpha values.
 */
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