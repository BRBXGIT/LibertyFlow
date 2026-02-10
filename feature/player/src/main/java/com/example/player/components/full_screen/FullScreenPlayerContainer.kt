@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.components.full_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.exoplayer.ExoPlayer
import com.example.design_system.components.bottom_sheets.quality_bs.VideoQualityBS
import com.example.player.components.player.Player
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

@Composable
internal fun FullScreenPlayerContainer(
    player: ExoPlayer,
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    BackHandler {
        onPlayerIntent(PlayerIntent.ToggleFullScreen)
        if (playerState.isLocked) onPlayerIntent(PlayerIntent.ToggleIsLocked)
    }

    if (playerState.isEpisodesDialogVisible) EpisodeDialog(onPlayerIntent, onPlayerEffect, playerState)

    if (playerState.isSettingsBSVisible) SettingsBS(playerState.playerSettings, onPlayerIntent)

    if (playerState.isQualityBSVisible) {
        VideoQualityBS(
            onItemClick = { quality -> onPlayerIntent(PlayerIntent.SaveQuality(quality)) },
            selectedQuality = playerState.playerSettings.quality,
            onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleQualityBS) }
        )
    }

    val pipManager = PipManager()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Player(player, playerState, pipManager)

        FullScreenPlayerController(playerState, pipManager, onPlayerEffect, onPlayerIntent)
    }
}