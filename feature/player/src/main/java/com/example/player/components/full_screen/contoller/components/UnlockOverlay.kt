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
private val UnlockLabel = R.string.unlock_label

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
            text = stringResource(UnlockLabel),
            icon = LibertyFlowIcons.Unlock,
            onClick = { onPlayerIntent(PlayerIntent.ToggleIsLocked) },
            type = ButtonWithIconType.Outlined
        )
    }
}