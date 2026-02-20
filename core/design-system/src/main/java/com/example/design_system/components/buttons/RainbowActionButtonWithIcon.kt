@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.buttons

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.containers.AnimatedBorderContainer
import com.example.design_system.containers.DownUpAnimatedContent
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

// UI Dimension constants (dp)
private val ICON_SIZE_DP = 22.dp
private val CONTENT_PADDING_DP = 2.dp

/**
 * The state and behavior for an action button.
 *
 * @property iconRes The [DrawableRes] icon representing the action.
 * @property labelRes The [StringRes] label describing the action.
 * @property isLoading When true, triggers the border animation to indicate background activity.
 * @property onClick The callback to execute when the button is pressed.
 */
data class ActionButtonState(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val isLoading: Boolean = false,
    val onClick: () -> Unit
)

/**
 * A high-visibility button that features a rotating "rainbow" gradient border.
 * * This component integrates [AnimatedBorderContainer] for the visual border effect
 * and [DownUpAnimatedContent] to smoothly transition between icon/text states.
 * The animation is automatically triggered if state.isLoading is true, making
 * it an ideal choice for primary "Call to Action" buttons that involve network requests.
 *
 * @param state The configuration and state object for the button.
 * @param showBorderAnimation Forces the border animation to run, regardless of the loading state.
 * @param modifier [Modifier] to be applied to the outer container.
 * @param shape The geometric shape of the button and its animated border.
 * @param borderColors The list of colors used for the rotating gradient. Defaults to a 6-color rainbow.
 */
@Composable
fun RainbowActionButtonWithIcon(
    state: ActionButtonState,
    showBorderAnimation: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = mShapes.extraLarge,
    borderColors: List<Color> = DefaultGradientColors
) {
    AnimatedBorderContainer(
        onClick = state.onClick,
        shape = shape,
        showAnimation = state.isLoading || showBorderAnimation,
        borderColors = borderColors,
        modifier = modifier
    ) {
        DownUpAnimatedContent(targetState = state) { targetState ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CONTENT_PADDING_DP),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(mDimens.spacingSmall, Alignment.CenterHorizontally)
            ) {
                LibertyFlowIcon(
                    icon = targetState.iconRes,
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

/**
 * A standard 6-color palette (Red, Orange, Yellow, Green, Blue, Purple)
 * used for the default "Rainbow" border effect.
 */
private val DefaultGradientColors = listOf(
    Color(0xFFE57373), Color(0xFFFFB74D), Color(0xFFFFF176),
    Color(0xFF81C784), Color(0xFF64B5F6), Color(0xFFBA68C8)
)