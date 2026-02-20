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
import com.example.design_system.theme.theme.mDimens
import com.example.player.components.common.ButtonWithAnimatedIcon
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

/**
 * Configuration defaults for the central playback controls.
 *
 * @property SkipIconSize The standard size for the 'Next' and 'Previous' icons.
 * @property PlayPauseIconSize The size for the primary Play/Pause toggle.
 * @property TOUCH_TARGET_MULTIPLAYER A multiplier applied to icon sizes to ensure
 * buttons meet minimum accessible touch target requirements (e.g., 48dp).
 */
private object CenterControlsDefaults {
    val SkipIconSize = 30.dp
    val PlayPauseIconSize = 34.dp

    const val TOUCH_TARGET_MULTIPLAYER = 2f
}

/**
 * The central transport control row for the video player.
 * * This component acts as the primary interaction hub, coordinating playlist navigation
 * and playback toggles. It intelligently manages its internal state to show either
 * playback controls or a loading indicator based on the current buffer status.
 *
 * @param episodeState The current playback status (Playing, Paused, or Loading).
 * @param currentEpisodeIndex The current position in the playlist (used for boundary checks).
 * @param totalEpisodes Total number of items in the playlist to determine if 'Next' is available.
 * @param isControllerVisible Controls whether the buttons are interactive and visible.
 * @param onPlayerIntent Dispatcher for sending playback actions back to the ViewModel.
 */
@Composable
internal fun BoxScope.CenterControls(
    episodeState: PlayerState.EpisodeState,
    currentEpisodeIndex: Int,
    totalEpisodes: Int,
    isControllerVisible: Boolean,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val hasPreviousEpisode = currentEpisodeIndex > 0
    val hasNextEpisode = currentEpisodeIndex < totalEpisodes - 1
    val isPlaying = episodeState == PlayerState.EpisodeState.Playing
    val isLoading = episodeState == PlayerState.EpisodeState.Loading

    Row(
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlayerIconButton(
            isAvailable = hasPreviousEpisode,
            icon = LibertyFlowIcons.Outlined.Previous,
            iconSize = CenterControlsDefaults.SkipIconSize,
            modifier = Modifier.size(CenterControlsDefaults.SkipIconSize * CenterControlsDefaults.TOUCH_TARGET_MULTIPLAYER),
            onClick = { onPlayerIntent(PlayerIntent.SkipEpisode(forward = false)) },
            isEnabled = isControllerVisible
        )

        if (isLoading) {
            CircularWavyProgressIndicator(
                modifier = Modifier.size(CenterControlsDefaults.PlayPauseIconSize)
            )
        } else {
            ButtonWithAnimatedIcon(
                icon = LibertyFlowIcons.Animated.PlayPause,
                atEnd = isPlaying,
                modifier = Modifier.size(CenterControlsDefaults.PlayPauseIconSize * CenterControlsDefaults.TOUCH_TARGET_MULTIPLAYER),
                onClick = {
                    if (isControllerVisible) onPlayerIntent(PlayerIntent.TogglePlayPause)
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
            icon = LibertyFlowIcons.Outlined.Next,
            iconSize = CenterControlsDefaults.SkipIconSize,
            modifier = Modifier.size(CenterControlsDefaults.SkipIconSize * CenterControlsDefaults.TOUCH_TARGET_MULTIPLAYER),
            onClick = { onPlayerIntent(PlayerIntent.SkipEpisode(forward = true)) },
            isEnabled = isControllerVisible
        )
    }
}