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
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.player.components.common.ButtonWithAnimatedIcon
import com.example.player.components.common.rememberControllerVisibility
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

private const val MAIN_BOX_Z_INDEX = 1f


/**
 * An interactive overlay for the Mini Player that provides quick access to transport controls.
 *
 * This controller handles:
 * 1. Visual feedback for the loading state via a [CircularWavyProgressIndicator].
 * 2. Media controls (Rewind, Play/Pause, Fast Forward).
 * 3. Expansion to Full Screen or closing the player entirely.
 *
 * @param episodeState The current playback status, used to toggle between the play/pause
 * icon and the loading indicator.
 * @param isControllerVisible Determines the opacity and interactivity of the buttons.
 * @param onPlayerIntent The callback used to dispatch [PlayerIntent]s back to the ViewModel.
 */
@Composable
internal fun BoxScope.MiniPlayerController(
    episodeState: PlayerState.EpisodeState,
    isControllerVisible: Boolean,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val visibility = rememberControllerVisibility(isControllerVisible)
    Box(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize()
            .zIndex(MAIN_BOX_Z_INDEX) // Ensure controller stays above the content
            .graphicsLayer { alpha = visibility.controlsAlpha }
            .clip(mShapes.small)
            .background(Color.Black.copy(alpha = visibility.overlayAlpha))
            .clickable { onPlayerIntent(PlayerIntent.ToggleControllerVisible) }
            .padding(mDimens.paddingExtraSmall)
    ) {
        ControllerButton(
            icon = LibertyFlowIcons.Outlined.FullScreen,
            visible = isControllerVisible,
            modifier = Modifier.align(Alignment.TopStart),
            onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) }
        )

        // Top-right exit/stop button
        ControllerButton(
            icon = LibertyFlowIcons.Outlined.CrossCircle,
            onClick = { onPlayerIntent(PlayerIntent.StopPlayer) },
            visible = isControllerVisible,
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
                icon = LibertyFlowIcons.Outlined.RewindBack,
                onClick = { onPlayerIntent(PlayerIntent.SeekForFiveSeconds(false)) },
                visible = isControllerVisible
            )

            if (episodeState == PlayerState.EpisodeState.Loading) {
                CircularWavyProgressIndicator(modifier = Modifier.size(ICON_SIZE.dp))
            } else {
                ButtonWithAnimatedIcon(
                    icon = LibertyFlowIcons.Animated.PlayPause,
                    atEnd = episodeState == PlayerState.EpisodeState.Playing,
                    modifier = Modifier.size((ICON_SIZE * 2).dp),
                    onClick = {
                        if (isControllerVisible) onPlayerIntent(PlayerIntent.TogglePlayPause)
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
                icon = LibertyFlowIcons.Outlined.Rewind,
                onClick = { onPlayerIntent(PlayerIntent.SeekForFiveSeconds(true)) },
                visible = isControllerVisible
            )
        }
    }
}

private const val ICON_SIZE = 24

/**
 * A specialized wrapper for player control icons.
 *
 * It ensures consistency in icon sizing and includes a safety check that prevents
 * click events from being dispatched if the button is currently invisible to the user.
 *
 * @param icon The drawable resource ID or icon object to display.
 * @param onClick The action to perform when the button is tapped.
 * @param visible If false, the button will ignore click events.
 * @param modifier Additional layout modifiers.
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
        LibertyFlowIcon(
            icon = icon,
            modifier = Modifier.size(ICON_SIZE.dp),
            tint = Color.White
        )
    }
}

@Preview
@Composable
private fun MiniPlayerControllerPreview() {
    Box {
        MiniPlayerController(
            episodeState = PlayerState.EpisodeState.Loading,
            isControllerVisible = true,
            onPlayerIntent = {}
        )
    }
}