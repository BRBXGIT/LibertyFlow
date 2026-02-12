@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.mini

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mShapes
import com.example.player.components.common.ButtonWithAnimatedIcon
import com.example.player.components.common.rememberControllerVisibility
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

private const val MAIN_BOX_Z_INDEX = 1f

private const val CONTROLLER_PADDING = 4

@Composable
internal fun BoxScope.MiniPlayerController(
    playerState: PlayerState,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val visibility = rememberControllerVisibility(playerState.isControllerVisible)
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize()
            .zIndex(MAIN_BOX_Z_INDEX) // Ensure controller stays above the content
            .graphicsLayer { alpha = visibility.controlsAlpha }
            .clip(mShapes.small)
            .background(Color.Black.copy(alpha = visibility.overlayAlpha))
            .clickable { onPlayerIntent(PlayerIntent.ToggleControllerVisible) }
            .padding(CONTROLLER_PADDING.dp)
    ) {
        ControllerButton(
            icon = LibertyFlowIcons.FullScreen,
            visible = playerState.isControllerVisible,
            modifier = Modifier.align(Alignment.TopStart),
            onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) }
        )

        // Top-right exit/stop button
        ControllerButton(
            icon = LibertyFlowIcons.CrossCircle,
            onClick = { onPlayerIntent(PlayerIntent.StopPlayer) },
            visible = playerState.isControllerVisible,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        // Bottom control row (Rewind - Play/Pause - Fast Forward)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            ControllerButton(
                icon = LibertyFlowIcons.RewindBack,
                onClick = { onPlayerIntent(PlayerIntent.SeekForFiveSeconds(false)) },
                visible = playerState.isControllerVisible
            )

            if (playerState.episodeState == PlayerState.EpisodeState.Loading) {
                CircularWavyProgressIndicator(modifier = Modifier.size(ICON_SIZE.dp))
            } else {
                ButtonWithAnimatedIcon(
                    iconId = LibertyFlowIcons.PlayPauseAnimated,
                    atEnd = playerState.episodeState == PlayerState.EpisodeState.Playing,
                    modifier = Modifier.size((ICON_SIZE * 2).dp),
                    onClick = {
                        if (playerState.isControllerVisible) onPlayerIntent(PlayerIntent.TogglePlayPause)
                    }
                ) { painter ->
                    Image(
                        painter = painter,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(ICON_SIZE.dp)
                    )
                }
            }

            ControllerButton(
                icon = LibertyFlowIcons.Rewind,
                onClick = { onPlayerIntent(PlayerIntent.SeekForFiveSeconds(true)) },
                visible = playerState.isControllerVisible
            )
        }
    }
}

private const val ICON_SIZE = 24

/**
 * A reusable icon button for player controls.
 * Uses a conditional click action based on visibility to prevent accidental triggers.
 */
@Composable
private fun ControllerButton(
    icon: Int,
    onClick: () -> Unit,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { if (visible) onClick() },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(ICON_SIZE.dp),
            tint = Color.White
        )
    }
}