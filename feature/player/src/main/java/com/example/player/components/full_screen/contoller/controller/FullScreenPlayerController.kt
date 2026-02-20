@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.full_screen.contoller.controller

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.player.R
import com.example.player.components.common.ControllerVisibility
import com.example.player.components.common.rememberControllerVisibility
import com.example.player.components.full_screen.contoller.components.CenterControls
import com.example.player.components.full_screen.contoller.components.Footer
import com.example.player.components.full_screen.contoller.components.Header
import com.example.player.components.full_screen.contoller.components.SkipOpeningButton
import com.example.player.components.full_screen.contoller.components.UnlockOverlay
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

// --- Constants & Configuration ---
private val EdgePadding = 32.dp
private val NoTitleLabel = R.string.no_title_provided_label

/**
 * The top-level controller for the full-screen video experience.
 *
 * This component manages the high-level UI state, switching between the [UnlockOverlay]
 * (when the player is locked) and the [MainControlsOverlay] (standard playback UI).
 * It also handles the global background tap gesture to toggle control visibility.
 *
 * @param playerState The global state containing playback data, settings, and UI flags.
 * @param onPlayerEffect Callback for one-time side effects (e.g., entering PiP).
 * @param onPlayerIntent Callback for user actions (e.g., play/pause, seeking).
 */
@Composable
internal fun FullScreenPlayerController(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()

    // Optimizing visibility: derived state ensures we don't recalculate logic unless these specific values change
    val visibility = rememberControllerVisibility(
        playerState.isControllerVisible,
        playerState.episodeTime.isScrubbing
    )

    // Derived data for UI: Prevents recalculating strings every time playerState.episodeTime updates
    val currentEpisode = remember(playerState.episodes, playerState.currentEpisodeIndex) {
        playerState.episodes.getOrNull(playerState.currentEpisodeIndex)
    }
    val title = currentEpisode?.name ?: stringResource(NoTitleLabel)
    val episodeNumber = remember(playerState.currentEpisodeIndex) { playerState.currentEpisodeIndex + 1 }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = { onPlayerIntent(PlayerIntent.ToggleControllerVisible) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        if (playerState.isLocked) {
            // Only visible when locked. Alpha is applied via graphicsLayer for performance.
            UnlockOverlay(
                onPlayerIntent = onPlayerIntent,
                alpha = visibility.controlsAlpha
            )
        } else {
            // Main UI controls
            MainControlsOverlay(
                isCropped = playerState.playerSettings.isCropped,
                totalEpisodes = playerState.episodes.size,
                currentEpisodeIndex = playerState.currentEpisodeIndex,
                episodeTime = playerState.episodeTime,
                episodeState = playerState.episodeState,
                isSkipOpeningButtonVisible = playerState.isSkipOpeningButtonVisible,
                isControllerVisible = playerState.isControllerVisible,
                title = title,
                episodeNumber = episodeNumber,
                visibility = visibility,
                contentPadding = systemBarsPadding,
                onPlayerEffect = onPlayerEffect,
                onPlayerIntent = onPlayerIntent,
            )
        }
    }
}

/**
 * The primary interaction layer of the player, assembling all UI components into a single overlay.
 *
 * This component is responsible for:
 * 1. **Layout Coordination**: Positioning the [Header], [Footer], [CenterControls], and [SkipOpeningButton].
 * 2. **Visual Effects**: Managing the background scrim (dimming) and hardware-accelerated fade transitions.
 * 3. **Safe Area Management**: Applying system bar insets to ensure controls are not obscured by
 * notches, status bars, or navigation pills.
 *
 * @param isCropped The current video scaling mode (Zoom/Fill vs. Fit).
 * @param totalEpisodes Total count of episodes in the current playlist for navigation logic.
 * @param currentEpisodeIndex The 0-based index of the currently playing episode.
 * @param episodeTime Data object containing current position, total duration, and scrubbing state.
 * @param episodeState The playback lifecycle state (e.g., Playing, Paused, or Loading).
 * @param title The formatted name of the current episode to be displayed in the header.
 * @param episodeNumber The human-readable number of the episode (usually index + 1).
 * @param visibility Animated alpha values for the control elements and the background dimming.
 * @param contentPadding Padding values provided by the system to avoid overlapping with system UI.
 * @param isSkipOpeningButtonVisible Whether the "Skip Intro" prompt should be active on screen.
 * @param isControllerVisible Global flag for UI visibility; affects button interactivity and hitboxes.
 * @param onPlayerEffect Dispatcher for one-time side effects like entering Picture-in-Picture.
 * @param onPlayerIntent The primary channel for sending user actions (intentions) to the ViewModel.
 */
@Composable
private fun MainControlsOverlay(
    isCropped: Boolean,
    totalEpisodes: Int,
    currentEpisodeIndex: Int,
    episodeTime: PlayerState.EpisodeTime,
    episodeState: PlayerState.EpisodeState,
    title: String,
    episodeNumber: Int,
    visibility: ControllerVisibility,
    contentPadding: PaddingValues,
    isSkipOpeningButtonVisible: Boolean,
    isControllerVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Using graphicsLayer for alpha to avoid invalidating the layout/draw phase of children
            .graphicsLayer { alpha = visibility.controlsAlpha }
            .background(Color.Black.copy(alpha = visibility.overlayAlpha))
            .padding(
                top = contentPadding.calculateTopPadding() + EdgePadding,
                bottom = contentPadding.calculateBottomPadding() + EdgePadding,
                start = EdgePadding,
                end = EdgePadding
            )
    ) {
        Header(
            title = title,
            episodeNumber = episodeNumber,
            isControllerVisible = isControllerVisible,
            onPlayerIntent = onPlayerIntent
        )

        CenterControls(
            episodeState = episodeState,
            currentEpisodeIndex = currentEpisodeIndex,
            totalEpisodes = totalEpisodes,
            isControllerVisible = isControllerVisible,
            onPlayerIntent = onPlayerIntent,
        )

        Footer(
            isControllerVisible = isControllerVisible,
            isCropped = isCropped,
            episodeTime = episodeTime,
            onPlayerEffect = onPlayerEffect,
            onPlayerIntent = onPlayerIntent
        )

        SkipOpeningButton(
            visible = isSkipOpeningButtonVisible,
            onPlayerIntent = onPlayerIntent
        )
    }
}