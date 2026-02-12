@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.full_screen.contoller.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.player.components.common.ButtonWithAnimatedIcon
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

private object CenterControlsDefaults {
    val SkipIconSize = 30.dp
    val PlayPauseIconSize = 34.dp
    val SpacingBetweenControls = 16.dp

    const val TOUCH_TARGET_MULTIPLAYER = 2f
}

@Composable
internal fun BoxScope.CenterControls(
    currentEpisodeIndex: Int,
    totalEpisodes: Int,
    playerState: PlayerState,
    isControllerVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val hasPreviousEpisode = currentEpisodeIndex > 0
    val hasNextEpisode = currentEpisodeIndex < totalEpisodes - 1
    val isPlaying = playerState.episodeState == PlayerState.EpisodeState.Playing
    val isLoading = playerState.episodeState == PlayerState.EpisodeState.Loading

    Row(
        horizontalArrangement = Arrangement.spacedBy(CenterControlsDefaults.SpacingBetweenControls),
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlayerIconButton(
            isAvailable = hasPreviousEpisode,
            icon = LibertyFlowIcons.Previous,
            iconSize = CenterControlsDefaults.SkipIconSize,
            modifier = Modifier.size(CenterControlsDefaults.SkipIconSize * CenterControlsDefaults.TOUCH_TARGET_MULTIPLAYER),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = false)) },
            isEnabled = isControllerVisible
        )

        if (isLoading) {
            CircularWavyProgressIndicator(
                modifier = Modifier.size(CenterControlsDefaults.PlayPauseIconSize)
            )
        } else {
            ButtonWithAnimatedIcon(
                iconId = LibertyFlowIcons.PlayPauseAnimated,
                atEnd = isPlaying,
                modifier = Modifier.size(CenterControlsDefaults.PlayPauseIconSize * CenterControlsDefaults.TOUCH_TARGET_MULTIPLAYER),
                onClick = {
                    if (playerState.isControllerVisible) onPlayerIntent(PlayerIntent.TogglePlayPause)
                }
            ) { painter ->
                Image(
                    painter = painter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(CenterControlsDefaults.PlayPauseIconSize)
                )
            }
        }

        PlayerIconButton(
            isAvailable = hasNextEpisode,
            icon = LibertyFlowIcons.Next,
            iconSize = CenterControlsDefaults.SkipIconSize,
            modifier = Modifier.size(CenterControlsDefaults.SkipIconSize * CenterControlsDefaults.TOUCH_TARGET_MULTIPLAYER),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = true)) },
            isEnabled = isControllerVisible
        )
    }
}