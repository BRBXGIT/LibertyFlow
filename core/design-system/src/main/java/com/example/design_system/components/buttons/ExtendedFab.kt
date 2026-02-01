package com.example.design_system.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.theme.mTypography

private val EasingLinearCubicBezier = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)
private val EasingEmphasizedCubicBezier = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

private val FabContainerWidth = 56.0.dp
private val ExtendedFabMinimumWidth = 80.dp
private val ExtendedFabIconSize = 24.dp
private val ExtendedFabIconPadding = 12.dp
private val ExtendedFabTextPadding = 16.dp
private val CollapsedIconSize = 0.dp
private val IconSize = 12.dp
private const val EXTENDED_FAB_ICON_SIZE_DIVIDER = 2

private const val FADE_OUT_DURATION = 100
private const val HORIZONTAL_DURATION = 500
private val ExtendedFabCollapseAnimation = fadeOut(
    animationSpec = tween(
        durationMillis = FADE_OUT_DURATION,
        easing = EasingLinearCubicBezier,
    ),
) + shrinkHorizontally(
    animationSpec = tween(
        durationMillis = HORIZONTAL_DURATION,
        easing = EasingEmphasizedCubicBezier,
    ),
    shrinkTowards = Alignment.Start,
)

private const val FADE_IN_DURATION = 200
private const val FADE_IN_DELAY = 100
private val ExtendedFabExpandAnimation = fadeIn(
    animationSpec = tween(
        durationMillis = FADE_IN_DURATION,
        delayMillis = FADE_IN_DELAY,
        easing = EasingLinearCubicBezier,
    ),
) + expandHorizontally(
    animationSpec = tween(
        durationMillis = HORIZONTAL_DURATION,
        easing = EasingEmphasizedCubicBezier,
    ),
    expandFrom = Alignment.Start,
)

private const val MIN_WIDTH_DURATION = 500
private const val EXPANDED_DURATION = 300
private const val COLLAPSED_DURATION = 900

private const val MIN_WIDTH_LABEL = "Min width animation"
private const val START_PADDING_LABEL = "Start padding label"

@Composable
fun LibertyFlowExtendedFAB(
    text: String,
    icon: Int,
    expanded: Boolean,
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        val minWidth by animateDpAsState(
            targetValue = if (expanded) ExtendedFabMinimumWidth else FabContainerWidth,
            animationSpec = tween(
                durationMillis = MIN_WIDTH_DURATION,
                easing = EasingEmphasizedCubicBezier,
            ),
            label = MIN_WIDTH_LABEL,
        )
        val startPadding by animateDpAsState(
            targetValue = if (expanded) ExtendedFabIconSize / EXTENDED_FAB_ICON_SIZE_DIVIDER else CollapsedIconSize,
            animationSpec = tween(
                durationMillis = if (expanded) EXPANDED_DURATION else COLLAPSED_DURATION,
                easing = EasingEmphasizedCubicBezier,
            ),
            label = START_PADDING_LABEL,
        )

        Row(
            modifier = Modifier
                .sizeIn(minWidth = minWidth)
                .padding(start = startPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(IconSize)
            )
            AnimatedVisibility(
                visible = expanded,
                enter = ExtendedFabExpandAnimation,
                exit = ExtendedFabCollapseAnimation,
            ) {
                Box(modifier = Modifier.padding(start = ExtendedFabIconPadding, end = ExtendedFabTextPadding)) {
                    Text(text = text, style = mTypography.labelLarge)
                }
            }
        }
    }
}