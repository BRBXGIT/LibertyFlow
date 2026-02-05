@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.containers

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mShapes

private const val rotationInitialValue = 0f
private const val rotationTargetValue = 360f
private const val rotationAnimationDuration = 4000

private const val BORDER_ALPHA_LABEL = "Animated border alpha"
private const val ROTATION_LABEL = "Rotation label"
private const val TRANSITION_LABEL = "Rotation transition label"

private const val ALPHA_VISIBLE = 1f
private const val ALPHA_HIDDEN = 0f

@Composable
fun AnimatedBorderContainer(
    modifier: Modifier = Modifier,
    bordersSize: Dp = 2.dp,
    shape: Shape = mShapes.medium,
    containerColor: Color = mColors.primary,
    contentColor: Color = mColors.onPrimary,
    showAnimation: Boolean,
    borderColors: List<Color>,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val transition = rememberInfiniteTransition(label = TRANSITION_LABEL)

    // Optimization: Only animate if showAnimation is true to save resources
    val rotation by transition.animateFloat(
        initialValue = rotationInitialValue,
        targetValue = rotationTargetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(rotationAnimationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ROTATION_LABEL
    )

    val alpha by animateFloatAsState(
        targetValue = if (showAnimation) ALPHA_VISIBLE else ALPHA_HIDDEN,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = BORDER_ALPHA_LABEL
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = containerColor // Set base color here
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .drawWithContent {
                    if (alpha > 0f) {
                        rotate(rotation) {
                            drawCircle(
                                brush = Brush.sweepGradient(borderColors.map { it.copy(alpha = alpha) }),
                                radius = size.width,
                                // We use SrcAtop or simply draw before content to avoid black artifacts
                                blendMode = BlendMode.SrcAtop
                            )
                        }
                    }
                    drawContent()
                }
                .padding(bordersSize) // Padding creates the "border" look by shrinking the content area
        ) {
            // Inner container to ensure the background covers the center
            Surface(
                shape = shape,
                color = containerColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(vertical = 4.dp) // Fine-tune internal spacing
                ) {
                    CompositionLocalProvider(LocalContentColor provides contentColor) {
                        content()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnimatedBorderContainerPreview() {
    LibertyFlowTheme {
        AnimatedBorderContainer(
            showAnimation = true,
            shape = mShapes.extraLarge,
            borderColors = listOf(
                Color(0xFFE57373), // Red
                Color(0xFFFFB74D), // Orange
                Color(0xFFFFF176), // Yellow
                Color(0xFF81C784), // Green
                Color(0xFF64B5F6), // Blue
                Color(0xFFBA68C8)  // Purple
            ),
            onClick = {}
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(LibertyFlowIcons.Filters),
                    contentDescription = null
                )

                Text(
                    text = "This is filters"
                )
            }
        }
    }
}