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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mAnimationTokens
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography

/**
 * Linear easing for opacity changes, ensuring consistent fade timing.
 */
private val EasingLinearCubicBezier = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)

/**
 * Standard Emphasized easing curve used for physical movement and width expansions.
 */
private val EasingEmphasizedCubicBezier = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

private val FabContainerWidth = 56.0.dp
private val ExtendedFabMinimumWidth = 80.dp
private val ExtendedFabIconSize = 24.dp
private val ExtendedFabIconPadding = 12.dp
private val CollapsedIconSize = 0.dp
private val IconSize = 12.dp
private const val EXTENDED_FAB_ICON_SIZE_DIVIDER = 2

private const val FADE_OUT_DURATION = 100
private const val HORIZONTAL_DURATION = 500

/**
 * Defines the exit transition when the FAB collapses into a circular shape.
 * Combines a quick fade-out with a slower horizontal shrink towards the start.
 */
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

/**
 * Defines the enter transition when the FAB expands to show its label.
 * Uses a delayed fade-in to ensure the text appears only after the container has
 * gained sufficient width.
 */
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

/**
 * A highly customizable Extended Floating Action Button that morphs between a circular
 * icon-only state and an expanded pill shape with text.
 *
 * This component handles the internal width and padding animations independently
 * of the [AnimatedVisibility] to ensure the container feels "physical" during
 * the transition.
 *
 * @param text The label displayed when the FAB is [expanded].
 * @param icon The DrawableRes icon displayed in both states.
 * @param expanded Controls the state of the FAB. `true` displays icon + text,
 * `false` displays icon only.
 * @param onClick Lambda triggered when the FAB is tapped.
 */
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
                durationMillis = mAnimationTokens.medium,
                easing = EasingEmphasizedCubicBezier,
            ),
            label = "Min width animation",
        )
        val startPadding by animateDpAsState(
            targetValue = if (expanded) ExtendedFabIconSize / EXTENDED_FAB_ICON_SIZE_DIVIDER else CollapsedIconSize,
            animationSpec = tween(
                durationMillis = if (expanded) mAnimationTokens.short else mAnimationTokens.extraLong,
                easing = EasingEmphasizedCubicBezier,
            ),
            label = "Start padding animation",
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
                Box(modifier = Modifier.padding(start = ExtendedFabIconPadding, end = mDimens.paddingMedium)) {
                    Text(text = text, style = mTypography.labelLarge)
                }
            }
        }
    }
}

@Preview
@Composable
private fun LibertyFlowExtendedFabPreview() {
    LibertyFlowTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                LibertyFlowExtendedFAB(
                    text = "Extended FAB",
                    icon = LibertyFlowIcons.Outlined.Rocket,
                    expanded = true,
                    onClick = {}
                )
            }
        ) { }
    }
}