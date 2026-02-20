package com.example.player.components.full_screen.contoller.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography
import com.example.player.R
import com.example.player.player.PlayerIntent

private object HeaderDefaults {
    val EpisodeLabelRes = R.string.episode_label
    const val MAX_TITLE_LINES = 1
}

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
internal fun BoxScope.Header(
    title: String,
    episodeNumber: Int,
    isControllerVisible: Boolean,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Section: Navigation and Episode Metadata
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)
        ) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Outlined.ArrowDown,
                onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) },
                isEnabled = isControllerVisible
            )

            Column(verticalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)) {
                Text(
                    text = title,
                    style = mTypography.bodyLarge.copy(
                        fontWeight = FontWeight.W600,
                        color = Color.White
                    ),
                    maxLines = HeaderDefaults.MAX_TITLE_LINES
                )
                Text(
                    text = "${stringResource(HeaderDefaults.EpisodeLabelRes)} $episodeNumber",
                    style = mTypography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }

        // Right Section: Supplementary Controls (Playlist and Settings)
        Row(horizontalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Outlined.Checklist,
                onClick = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) },
                isEnabled = isControllerVisible
            )
            PlayerIconButton(
                icon = LibertyFlowIcons.Outlined.Settings,
                onClick = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) },
                isEnabled = isControllerVisible
            )
        }
    }
}