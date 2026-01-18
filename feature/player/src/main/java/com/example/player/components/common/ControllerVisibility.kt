@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.design_system.theme.mMotionScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.State

private const val ANIMATED_CONTROLLER_ALPHA_LABEL = "Animated alpha for controller"
private const val ANIMATED_CONTROLLER_TINT_ALPHA = "Animated alpha for controller tint"

private const val VISIBLE = 1f
private const val INVISIBLE = 0f
private const val ALMOST_INVISIBLE = 0.7f

internal class ControllerVisibility(
    alphaState: State<Float>,
    tintState: State<Float>
) {
    val alpha by alphaState
    val tint by tintState
}

@Composable
internal fun rememberControllerVisibility(isVisible: Boolean): ControllerVisibility {
    val motionScheme = mMotionScheme
    
    val alpha = animateFloatAsState(
        targetValue = if (isVisible) VISIBLE else INVISIBLE,
        animationSpec = motionScheme.slowEffectsSpec(),
        label = ANIMATED_CONTROLLER_ALPHA_LABEL
    )
    
    val tint = animateFloatAsState(
        targetValue = if (isVisible) ALMOST_INVISIBLE else INVISIBLE,
        animationSpec = motionScheme.slowEffectsSpec(),
        label = ANIMATED_CONTROLLER_TINT_ALPHA
    )

    return remember(alpha, tint) { ControllerVisibility(alpha, tint) }
}