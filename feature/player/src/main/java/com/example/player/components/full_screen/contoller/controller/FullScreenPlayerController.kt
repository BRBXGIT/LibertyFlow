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
import com.example.player.components.full_screen.pip.PipManager
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

// --- Constants & Configuration ---
private val EdgePadding = 32.dp
private val NoTitleLabel = R.string.no_title_provided_label
private const val ONE = 1

@Composable
internal fun FullScreenPlayerController(
    playerState: PlayerState,
    pipManager: PipManager,
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
    val episodeNumber = remember(playerState.currentEpisodeIndex) { playerState.currentEpisodeIndex + ONE }

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
                title = title,
                episodeNumber = episodeNumber,
                playerState = playerState,
                visibility = visibility,
                pipManager = pipManager,
                contentPadding = systemBarsPadding,
                onPlayerEffect = onPlayerEffect,
                onPlayerIntent = onPlayerIntent
            )
        }
    }
}

@Composable
private fun MainControlsOverlay(
    title: String,
    episodeNumber: Int,
    playerState: PlayerState,
    visibility: ControllerVisibility,
    pipManager: PipManager,
    contentPadding: PaddingValues,
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
            isControllerVisible = playerState.isControllerVisible,
            onPlayerIntent = onPlayerIntent
        )

        CenterControls(
            currentEpisodeIndex = playerState.currentEpisodeIndex,
            totalEpisodes = playerState.episodes.size,
            playerState = playerState,
            isControllerVisible = playerState.isControllerVisible,
            onPlayerEffect = onPlayerEffect,
            onPlayerIntent = onPlayerIntent
        )

        Footer(
            playerState = playerState,
            pipManager = pipManager,
            onPlayerEffect = onPlayerEffect,
            onPlayerIntent = onPlayerIntent
        )

        SkipOpeningButton(
            visible = playerState.isSkipOpeningButtonVisible,
            onPlayerIntent = onPlayerIntent
        )
    }
}