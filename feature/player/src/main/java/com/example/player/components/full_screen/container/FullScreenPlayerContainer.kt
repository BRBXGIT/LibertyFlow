@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.components.full_screen.container

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.exoplayer.ExoPlayer
import com.example.player.components.full_screen.components.EpisodeDialog
import com.example.player.components.full_screen.contoller.controller.FullScreenPlayerController
import com.example.player.components.full_screen.pip.PipManager
import com.example.player.components.full_screen.components.SettingsDialog
import com.example.player.components.full_screen.components.SleepTimerDialog
import com.example.player.components.full_screen.components.VideoQualityDialog
import com.example.player.components.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

@Composable
internal fun FullScreenPlayerContainer(
    player: ExoPlayer,
    playerState: PlayerState,
    pipManager: PipManager,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    BackHandler {
        onPlayerIntent(PlayerIntent.ToggleFullScreen)
        if (playerState.isLocked) onPlayerIntent(PlayerIntent.ToggleIsLocked)
    }

    if (playerState.isSettingsDialogVisible) SettingsDialog(playerState.playerSettings, onPlayerIntent)

    if (playerState.isQualityDialogVisible) {
        VideoQualityDialog(
            selectedQuality = playerState.playerSettings.quality,
            onPlayerIntent = onPlayerIntent
        )
    }

    if (playerState.isSleepDialogVisible) {
        SleepTimerDialog(
            currentSleepTime = playerState.currentSleepTime,
            onPlayerIntent = onPlayerIntent
        )
    }

    if (playerState.isEpisodesDialogVisible) {
        EpisodeDialog(
            onPlayerIntent = onPlayerIntent,
            currentEpisodeIndex = playerState.currentEpisodeIndex,
            episodes = playerState.episodes
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Player(
            uiPlayerState = playerState.uiPlayerState,
            isCropped = playerState.playerSettings.isCropped,
            player = player,
            pipManager = pipManager
        )

        FullScreenPlayerController(playerState, onPlayerEffect, onPlayerIntent)
    }
}