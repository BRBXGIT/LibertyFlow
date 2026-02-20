@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.player

import android.content.pm.ActivityInfo
import android.view.Window
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.exoplayer.ExoPlayer
import com.example.common.ui_helpers.utils.findActivity
import com.example.player.components.full_screen.container.FullScreenPlayerContainer
import com.example.player.components.full_screen.pip.PipManager
import com.example.player.components.mini.MiniPlayerContainer
import kotlinx.coroutines.flow.Flow

/**
 * The top-level orchestrator for the player UI.
 * * Based on the [PlayerState.uiPlayerState], it switches between a full-screen experience
 * and a mini-player (floating/bottom-bar) experience. It also initializes the
 * [PipManager] to ensure the lifecycle of Picture-in-Picture is handled correctly.
 *
 * @param playerEffects A stream of one-time events from the ViewModel.
 * @param playerState The current state of playback and UI visibility.
 * @param player The [ExoPlayer] instance to be rendered.
 * @param navBarVisible Whether the app's bottom navigation bar is currently shown (relevant for Mini player layout).
 * @param onPlayerEffect Callback to handle effects at the Activity/Screen level.
 * @param onPlayerIntent The single entry point for sending user actions back to the ViewModel.
 */
@Composable
fun PlayerContainer(
    playerEffects: Flow<PlayerEffect>,
    playerState: PlayerState,
    player: ExoPlayer,
    navBarVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val uiPlayerState = playerState.uiPlayerState

    HandleLandscape(uiPlayerState)

    val pipManager = remember { PipManager() }
    HandlePlayerEffects(playerEffects, pipManager)

    when (uiPlayerState) {
        PlayerState.UiPlayerState.Full -> {
            FullScreenPlayerContainer(
                player = player,
                playerState = playerState,
                onPlayerEffect = onPlayerEffect,
                onPlayerIntent = onPlayerIntent,
                pipManager = pipManager
            )
        }

        PlayerState.UiPlayerState.Mini -> {
            MiniPlayerContainer(
                isCropped = playerState.playerSettings.isCropped,
                uiPlayerState = playerState.uiPlayerState,
                isControllerVisible = playerState.isControllerVisible,
                episodeState = playerState.episodeState,
                navBarVisible = navBarVisible,
                player = player,
                onPlayerIntent = onPlayerIntent
            )
        }

        PlayerState.UiPlayerState.Closed -> {} // Nothing
    }
}

/**
 * A side-effect handler that manages the physical device orientation and system UI.
 * * When entering [PlayerState.UiPlayerState.Full], it forces the Activity into
 * landscape mode and hides the status/navigation bars. When moving to other states,
 * it restores portrait orientation and shows the system bars.
 */
@Composable
private fun HandleLandscape(uiPlayerState: PlayerState.UiPlayerState) {
    val context = LocalContext.current

    // LaunchedEffect ensures the side-effect runs only when uiPlayerState changes
    LaunchedEffect(uiPlayerState) {
        val activity = context.findActivity()
        val window = activity?.window ?: return@LaunchedEffect

        val isLandscape = uiPlayerState == PlayerState.UiPlayerState.Full

        // Update System UI visibility (status and navigation bars)
        toggleSystemBars(window, hide = isLandscape)

        // Force orientation change based on player mode
        activity.requestedOrientation = if (isLandscape) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}

/**
 * Manages the visibility and behavior of the system bars.
 */
private fun toggleSystemBars(window: Window, hide: Boolean) {
    val controller = WindowInsetsControllerCompat(window, window.decorView)

    if (hide) {
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        controller.show(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

/**
 * Collects and executes [PlayerEffect]s within the Composable lifecycle.
 * * Specifically, it handles the [PlayerEffect.TryPipEnterPip] by delegating
 * the request to the provided [pipManager].
 */
@Composable
private fun HandlePlayerEffects(
    effects: Flow<PlayerEffect>,
    pipManager: PipManager,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when(effect) {
                PlayerEffect.TryPipEnterPip -> pipManager.tryEnterPip(context)
            }
        }
    }
}