package com.example.player.components.full_screen.contoller.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.design_system.components.buttons.ButtonWithIcon
import com.example.design_system.components.buttons.ButtonWithIconType
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.player.R
import com.example.player.player.PlayerIntent

private val EdgePadding = 32.dp
private val UnlockLabelRes = R.string.unlock_label

/**
 * A specialized overlay that appears only when the player is in a 'Locked' state.
 *
 * This component provides a single point of interaction to unlock the player controls.
 * It is positioned at the bottom center of the screen to remain accessible but
 * out of the way of the primary video content.
 *
 * @param onPlayerIntent Callback used to dispatch [PlayerIntent.ToggleIsLocked],
 * which restores full interactivity to the player.
 * @param alpha The current animated opacity level. This is typically tied to the
 * visibility of the main controller or a specific "wake" gesture.
 */
@Composable
internal fun UnlockOverlay(
    onPlayerIntent: (PlayerIntent) -> Unit,
    alpha: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { this.alpha = alpha }
            .padding(bottom = EdgePadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        ButtonWithIcon(
            text = stringResource(UnlockLabelRes),
            icon = LibertyFlowIcons.Outlined.Unlock,
            onClick = { onPlayerIntent(PlayerIntent.ToggleIsLocked) },
            type = ButtonWithIconType.Outlined
        )
    }
}