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
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mTypography
import com.example.player.R
import com.example.player.player.PlayerIntent

private val EpisodeLabel = R.string.episode_label
private val IconSpacing = 4.dp
private val HeaderSpacing = 4.dp


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
        // Left Side: Back & Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IconSpacing)
        ) {
            PlayerIconButton(
                icon = LibertyFlowIcons.ArrowDown,
                onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) },
                isEnabled = isControllerVisible
            )
            Column(verticalArrangement = Arrangement.spacedBy(HeaderSpacing)) {
                Text(
                    text = title,
                    style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600, color = Color.White),
                    maxLines = 1
                )
                Text(
                    text = "${stringResource(EpisodeLabel)} $episodeNumber",
                    style = mTypography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }

        // Right Side: Quick Settings
        Row(horizontalArrangement = Arrangement.spacedBy(IconSpacing)) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Checklist,
                onClick = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) },
                isEnabled = isControllerVisible
            )
            PlayerIconButton(
                icon = LibertyFlowIcons.Settings,
                onClick = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) },
                isEnabled = isControllerVisible
            )
        }
    }
}