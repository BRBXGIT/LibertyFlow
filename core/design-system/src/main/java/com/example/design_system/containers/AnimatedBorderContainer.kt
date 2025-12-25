package com.example.design_system.containers

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
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
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes

private const val rotationInitialValue = 0f
private const val rotationTargetValue = 360f
private const val rotationAnimationDuration = 4000

@Composable
fun AnimatedBorderContainer(
    modifier: Modifier = Modifier,
    bordersSize: Dp = 2.dp,
    shape: Shape = mShapes.medium,
    containerColor: Color = mColors.primary,
    contentColor: Color = mColors.onPrimary,
    brush: Brush,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val transition = rememberInfiniteTransition()
    val rotation = transition.animateFloat(
        initialValue = rotationInitialValue,
        targetValue = rotationTargetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(rotationAnimationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape
    ) {
        Surface(
            shape = shape,
            modifier = Modifier
                .clipToBounds()
                .padding(bordersSize)
                .drawWithContent {
                    rotate(rotation.value) {
                        drawCircle(
                            brush = brush,
                            radius = size.width,
                            blendMode = BlendMode.SrcIn
                        )
                    }
                    drawContent()
                }
        ) {
            Box(
                modifier = Modifier.background(containerColor),
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    content()
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
            shape = mShapes.extraLarge,
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFE57373), // Red
                    Color(0xFFFFB74D), // Orange
                    Color(0xFFFFF176), // Yellow
                    Color(0xFF81C784), // Green
                    Color(0xFF64B5F6), // Blue
                    Color(0xFFBA68C8)  // Purple
                )
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