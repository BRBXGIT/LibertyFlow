package com.example.player.components.player

import android.graphics.Rect
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.player.components.full_screen.pip.PipManager
import com.example.player.player.PlayerState

private const val zIndex = 0f

@OptIn(UnstableApi::class)
@Composable
internal fun BoxScope.Player(
    player: ExoPlayer,
    playerState: PlayerState,
    pipManager: PipManager? = null
) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
                resizeMode = getResizeMode(playerState.isCropped, playerState.uiPlayerState)
                useController = false
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { it.resizeMode = getResizeMode(playerState.isCropped, playerState.uiPlayerState) },
        modifier = Modifier
            .align(Alignment.Center)
            .zIndex(zIndex)
            .fillMaxSize()
            .onGloballyPositioned {
                if (pipManager != null) {
                    pipManager.videoViewBounce = run {
                        val boundsInWindow = it.boundsInWindow()
                        Rect(
                            boundsInWindow.left.toInt(),
                            boundsInWindow.top.toInt(),
                            boundsInWindow.right.toInt(),
                            boundsInWindow.bottom.toInt()
                        )
                    }
                }
            }
    )
}

@OptIn(UnstableApi::class)
private fun getResizeMode(cropped: Boolean, uiPlayerState: PlayerState.UiPlayerState): Int {
    if (uiPlayerState == PlayerState.UiPlayerState.Mini) return AspectRatioFrameLayout.RESIZE_MODE_ZOOM

    return when(cropped) {
        true -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        false -> AspectRatioFrameLayout.RESIZE_MODE_FIT
    }
}