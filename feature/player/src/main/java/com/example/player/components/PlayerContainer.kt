@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.components

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
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState

@Composable
fun PlayerContainer(
    playerState: PlayerState,
    player: ExoPlayer,
    navBarVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    val uiPlayerState = playerState.uiPlayerState

    HandleLandscape(uiPlayerState)

    when (uiPlayerState) {
        PlayerState.UiPlayerState.Full -> {
            FullScreenPlayerContainer(
                player = player,
                playerState = playerState,
                onPlayerEffect = onPlayerEffect
            )
        }

        PlayerState.UiPlayerState.Mini -> {
            MiniPlayerContainer(
                player = player,
                navBarVisible = navBarVisible,
                playerState = playerState,
                onPlayerEffect = onPlayerEffect,
            )
        }

        PlayerState.UiPlayerState.Closed -> {}
    }
}

@Composable
private fun HandleLandscape(uiPlayerState: PlayerState.UiPlayerState) {
    val window = LocalContext.current.findActivity()?.window

    LaunchedEffect(uiPlayerState) {
        window?.let {
            val isLandscape = uiPlayerState == PlayerState.UiPlayerState.Full
            changeSystemBars(it, isLandscape)

            val activity = it.context.findActivity()
            activity?.requestedOrientation = if (isLandscape) {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private fun changeSystemBars(window: Window, hide: Boolean) {
    val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)

    when(hide) {
        true -> {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        false -> {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())

            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }
}