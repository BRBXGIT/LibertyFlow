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

private val SkipIconSize = 30.dp
private val PlayPauseIconSize = 34.dp
private const val ZERO = 0
private const val ONE = 1

@Composable
internal fun BoxScope.CenterControls(
    currentEpisodeIndex: Int,
    totalEpisodes: Int,
    playerState: PlayerState,
    isControllerVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlayerIconButton(
            isAvailable = currentEpisodeIndex > ZERO,
            icon = LibertyFlowIcons.Previous,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipIconSize * 2),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = false)) },
            isEnabled = isControllerVisible
        )

        if (playerState.episodeState == PlayerState.EpisodeState.Loading) {
            CircularWavyProgressIndicator(modifier = Modifier.size(PlayPauseIconSize))
        } else {
            ButtonWithAnimatedIcon(
                iconId = LibertyFlowIcons.PlayPauseAnimated,
                atEnd = playerState.episodeState == PlayerState.EpisodeState.Playing,
                modifier = Modifier.size((PlayPauseIconSize * 2)),
                onClick = {
                    if (playerState.isControllerVisible) onPlayerIntent(PlayerIntent.TogglePlayPause)
                }
            ) { painter ->
                Image(
                    painter = painter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(PlayPauseIconSize)
                )
            }
        }
        PlayerIconButton(
            isAvailable = currentEpisodeIndex < totalEpisodes - ONE,
            icon = LibertyFlowIcons.Next,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipIconSize * 2),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = true)) },
            isEnabled = isControllerVisible
        )
    }
}