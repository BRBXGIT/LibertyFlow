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

// TODO: Kdoc
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
                icon = LibertyFlowIcons.Outlined.AlarmSleep,
                onClick = { onPlayerIntent(PlayerIntent.SetSleepTimer(1, 1)) },
                isEnabled = isControllerVisible
            )
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