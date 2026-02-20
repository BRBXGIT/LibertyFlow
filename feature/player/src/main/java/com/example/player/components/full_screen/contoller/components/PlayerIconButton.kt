package com.example.player.components.full_screen.contoller.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.design_system.components.icons.LibertyFlowIcon

/**
 * A specialized [IconButton] for video player controls that supports conditional availability.
 *
 * This component distinguishes between two states of inactivity:
 * 1. **Enabled/Disabled**: Controlled by [isEnabled], typically used to prevent interaction
 * when the player UI is fading out or locked.
 * 2. **Available/Unavailable**: Controlled by [isAvailable], used when an action doesn't
 * exist in the current context (e.g., 'Previous' button on the first episode).
 * Unavailable buttons are tinted gray to signify they cannot be used.
 *
 * @param icon The resource ID of the icon to be rendered.
 * @param onClick The action to execute when the button is tapped.
 * @param modifier Modifiers applied to the outer [IconButton].
 * @param iconSize Optional specific size for the inner icon. If [Dp.Unspecified],
 * the icon will use its own default sizing.
 * @param isEnabled If false, the button will be non-interactive but may keep its color.
 * @param isAvailable If false, the button will be tinted gray and will not trigger [onClick].
 */
@Composable
internal fun PlayerIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = Dp.Unspecified,
    isEnabled: Boolean = true,
    isAvailable: Boolean = true
) {
    IconButton(
        onClick = { if (isAvailable) onClick() },
        modifier = modifier,
        enabled = isEnabled
    ) {
        LibertyFlowIcon(
            icon = icon,
            modifier = if (iconSize != Dp.Unspecified) Modifier.size(iconSize) else Modifier,
            tint = if (isAvailable) Color.White else Color.Gray
        )
    }
}