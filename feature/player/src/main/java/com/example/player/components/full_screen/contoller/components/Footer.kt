package com.example.player.components.full_screen.contoller.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mTypography
import com.example.player.components.common.ButtonWithAnimatedIcon
import com.example.player.components.full_screen.pip.PipManager
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

private val HeaderSpacing = 4.dp

private val IconSpacing = 4.dp

@Composable
internal fun BoxScope.Footer(
    playerState: PlayerState,
    pipManager: PipManager,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    var scrubPosition by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.spacedBy(HeaderSpacing)
    ) {
        // Time and Actions Row
        TimeAndActionsRow(
            playerState = playerState,
            scrubPosition = scrubPosition,
            pipManager = pipManager,
            onPlayerIntent = onPlayerIntent
        )

        PlayerSlider(
            currentPosition = playerState.episodeTime.current,
            totalDuration = playerState.episodeTime.total,
            scrubPosition = scrubPosition,
            onScrubbing = {
                onPlayerIntent(PlayerIntent.SetIsScrubbing(true))
                scrubPosition = it
            },
            onSeekFinished = {
                onPlayerIntent(PlayerIntent.SetIsScrubbing(false))
                onPlayerEffect(PlayerEffect.SeekTo(it))
                scrubPosition = null
            }
        )
    }
}

@Composable
private fun TimeAndActionsRow(
    playerState: PlayerState,
    scrubPosition: Long?,
    pipManager: PipManager,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Optimized Time Label: Separated to limit recompositions when only time changes
        val displayTime = scrubPosition ?: playerState.episodeTime.current
        Text(
            text = "${displayTime.formatMinSec()} / ${playerState.episodeTime.total.formatMinSec()}",
            style = mTypography.bodyMedium.copy(color = Color.White)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(IconSpacing)) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Lock,
                onClick = { onPlayerIntent(PlayerIntent.ToggleIsLocked) },
                isEnabled = playerState.isControllerVisible
            )

            ButtonWithAnimatedIcon(
                iconId = LibertyFlowIcons.CropAnimated,
                atEnd = !playerState.isCropped,
                onClick = { onPlayerIntent(PlayerIntent.ToggleCropped) }
            ) { painter ->
                Image(
                    painter = painter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }

            PlayerIconButton(
                icon = LibertyFlowIcons.Pip,
                onClick = {
                    onPlayerIntent(PlayerIntent.TurnOffController)
                    if (pipManager.isPipSupported(context)) {
                        pipManager.updatedPipParams()?.let { params ->
                            (context as? Activity)?.enterPictureInPictureMode(params)
                        }
                    }
                },
                isEnabled = playerState.isControllerVisible
            )
        }
    }
}

private const val TRACK_ALPHA = 0.24f
private const val SAFE_TOTAL_DURATION = 0L
private val SliderHeight = 1.dp

@Composable
private fun PlayerSlider(
    currentPosition: Long,
    totalDuration: Long,
    scrubPosition: Long?,
    onScrubbing: (Long) -> Unit,
    onSeekFinished: (Long) -> Unit
) {
    // We use derived state to avoid heavy math on every recomposition if inputs didn't change
    val safeTotalDuration = remember(totalDuration) { totalDuration.coerceAtLeast(SAFE_TOTAL_DURATION).toFloat() }
    val displayPosition = (scrubPosition ?: currentPosition).toFloat()

    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            // Key optimization: the lambda version of progress avoids full component recomposition
            progress = { if (safeTotalDuration > 0) displayPosition / safeTotalDuration else 0f },
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            trackColor = Color.White.copy(alpha = TRACK_ALPHA),
            strokeCap = StrokeCap.Round
        )

        Slider(
            value = displayPosition,
            valueRange = 0f..safeTotalDuration,
            modifier = Modifier
                .fillMaxWidth()
                .height(SliderHeight),
            onValueChange = { newValue -> onScrubbing(newValue.toLong()) },
            onValueChangeFinished = { onSeekFinished(displayPosition.toLong()) },
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            )
        )
    }
}

// --- Utils ---
private fun Long.formatMinSec(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}