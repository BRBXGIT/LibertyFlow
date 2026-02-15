package com.example.design_system.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography

/**
 * Defines the visual style of the [ButtonWithIcon].
 */
enum class ButtonWithIconType { Filled, Outlined }

/**
 * A custom button that integrates an icon and text with consistent spacing.
 *
 * This component acts as a factory that switches between a [Button] and an
 * [OutlinedButton] based on the provided [type], while maintaining identical
 * internal content layout.
 *
 * @param text The label displayed next to the icon.
 * @param icon The DrawableRes ID for the icon to be displayed at the start.
 * @param modifier [Modifier] to be applied to the button container.
 * @param type The visual variant ([ButtonWithIconType.Filled] or [ButtonWithIconType.Outlined]).
 * @param onClick Lambda triggered when the button is pressed.
 */
@Composable
fun ButtonWithIcon(
    text: String,
    icon: Int,
    modifier: Modifier = Modifier,
    type: ButtonWithIconType = ButtonWithIconType.Filled,
    onClick: () -> Unit
) {
    when(type) {
        ButtonWithIconType.Filled -> {
            Button(
                onClick = onClick,
                modifier = modifier,
            ) {
                ButtonContent(icon, text)
            }
        }
        ButtonWithIconType.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
            ) {
                ButtonContent(icon, text)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    icon: Int,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(mDimens.paddingMedium)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            style = mTypography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun ButtonWithIconPreview() {
    LibertyFlowTheme {
        ButtonWithIcon(
            text = "Button with icon",
            icon = LibertyFlowIcons.Outlined.Rocket,
            type = ButtonWithIconType.Filled,
            onClick = {}
        )
    }
}