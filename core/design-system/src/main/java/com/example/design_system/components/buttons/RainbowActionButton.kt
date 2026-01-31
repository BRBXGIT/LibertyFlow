@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.design_system.containers.AnimatedBorderContainer
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import kotlin.collections.map

// Animation constants
private const val ANIMATION_DURATION = 300
private const val OFFSET_DIVIDER = 2
private const val ALPHA_VISIBLE = 1f
private const val ALPHA_HIDDEN = 0f

// UI Dimension constants (dp)
private val ICON_SIZE_DP = 22.dp
private val CONTENT_PADDING_DP = 8.dp
private val SPACING_DP = 8.dp

// Labels for Compose tooling
private const val BORDER_ALPHA_LABEL = "Animated border alpha"
private const val CONTENT_TRANSITION_LABEL = "Button content transition"

data class ActionButtonState(
    val iconRes: Int,
    val labelRes: Int,
    val isLoading: Boolean = false,
    val onClick: () -> Unit
)

// Reusable button component which shows rainbow animation, for e.x. if something loading
@Composable
fun RainbowActionButton(
    state: ActionButtonState,
    showBorderAnimation: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = mShapes.extraLarge,
    borderColors: List<Color> = DefaultGradientColors
) {
    AnimatedBorderContainer(
        onClick = state.onClick,
        shape = shape,
        brush = animatedBorderBrush(state.isLoading || showBorderAnimation, borderColors),
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                slideInVertically(
                    animationSpec = tween(ANIMATION_DURATION),
                    initialOffsetY = { it / OFFSET_DIVIDER }
                ) + fadeIn() togetherWith
                        slideOutVertically(
                            animationSpec = tween(ANIMATION_DURATION),
                            targetOffsetY = { -it / OFFSET_DIVIDER }
                        ) + fadeOut()
            },
            label = CONTENT_TRANSITION_LABEL,
            modifier = Modifier.fillMaxWidth()
        ) { targetState ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CONTENT_PADDING_DP),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SPACING_DP)
                ) {
                    Icon(
                        painter = painterResource(targetState.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(ICON_SIZE_DP)
                    )
                    Text(
                        text = stringResource(targetState.labelRes),
                        style = mTypography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun animatedBorderBrush(
    showAnimation: Boolean,
    colors: List<Color>
): Brush {
    val alpha by animateFloatAsState(
        targetValue = if (showAnimation) ALPHA_VISIBLE else ALPHA_HIDDEN,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = BORDER_ALPHA_LABEL
    )

    return Brush.sweepGradient(
        colors = colors.map { it.copy(alpha = alpha) }
    )
}

private val DefaultGradientColors = listOf(
    Color(0xFFE57373), Color(0xFFFFB74D), Color(0xFFFFF176),
    Color(0xFF81C784), Color(0xFF64B5F6), Color(0xFFBA68C8)
)