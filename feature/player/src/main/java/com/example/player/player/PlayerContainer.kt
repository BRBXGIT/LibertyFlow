@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.player

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.Window
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.exoplayer.ExoPlayer
import com.example.player.components.full_screen.container.FullScreenPlayerContainer
import com.example.player.components.mini.MiniPlayerContainer

@Composable
fun PlayerContainer(
    playerState: PlayerState,
    player: ExoPlayer,
    navBarVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val uiPlayerState = playerState.uiPlayerState

    HandleLandscape(uiPlayerState)

    when (uiPlayerState) {
        PlayerState.UiPlayerState.Full -> {
            FullScreenPlayerContainer(
                player = player,
                playerState = playerState,
                onPlayerEffect = onPlayerEffect,
                onPlayerIntent = onPlayerIntent
            )
        }

        PlayerState.UiPlayerState.Mini -> {
            MiniPlayerContainer(
                player = player,
                navBarVisible = navBarVisible,
                playerState = playerState,
                onPlayerEffect = onPlayerEffect,
                onPlayerIntent = onPlayerIntent
            )
        }

        PlayerState.UiPlayerState.Closed -> {}
    }
}

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
 * Utility function to find the host Activity from a Context.
 */
private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
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