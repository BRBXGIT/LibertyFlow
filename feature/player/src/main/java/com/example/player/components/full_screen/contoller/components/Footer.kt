package com.example.player.components.full_screen.contoller.components

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
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography
import com.example.player.components.common.ButtonWithAnimatedIcon
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

/**
 * Configuration defaults for the player footer, including timing constants
 * and visual alpha levels for the progress track.
 */
private object FooterDefaults {
    val SliderHeight = 1.dp
    const val TRACK_ALPHA = 0.24f
    const val SAFE_TOTAL_DURATION = 0L
    const val MILLIS_TO_SECONDS = 1000L
    const val SECONDS_IN_MINUTE = 60
}

/**
 * The bottom control area of the player, housing the seek bar, timestamps,
 * and secondary action buttons (Crop, PiP, Lock).
 *
 * This component manages scrubPosition locally to provide instantaneous
 * visual feedback while the user is dragging the slider, preventing
 * "jumping" artifacts caused by network latency or state synchronization.
 *
 * @param isControllerVisible Controls the interactivity and visibility of the icons.
 * @param isCropped The current aspect ratio preference, used to toggle the crop icon state.
 * @param episodeTime Timing data (current, total, isScrubbing) from the [PlayerState].
 * @param onPlayerEffect Dispatcher for one-time side effects like entering PiP.
 * @param onPlayerIntent Dispatcher for playback and UI state changes.
 */
@Composable
internal fun BoxScope.Footer(
    isControllerVisible: Boolean,
    isCropped: Boolean,
    episodeTime: PlayerState.EpisodeTime,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    // Stores the temporary position while the user is dragging the slider
    var scrubPosition by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)
    ) {
        ActionsRow(
            onPlayerIntent = onPlayerIntent,
            onPlayerEffect = onPlayerEffect,
            isCropped = isCropped,
            isControllerVisible = isControllerVisible
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(mDimens.spacingMedium)
        ) {
            // Priority given to scrubPosition to ensure smooth visual feedback during seeking
            val currentPos = scrubPosition ?: episodeTime.current
            val totalPos = episodeTime.total

            Text(
                text = "${currentPos.formatMinSec()} / ${totalPos.formatMinSec()}",
                style = mTypography.bodyMedium.copy(color = Color.White)
            )

            PlayerSlider(
                currentPosition = episodeTime.current,
                totalDuration = totalPos,
                scrubPosition = scrubPosition,
                onScrubbing = {
                    onPlayerIntent(PlayerIntent.SetIsScrubbing(true))
                    scrubPosition = it
                },
                onSeekFinished = {
                    onPlayerIntent(PlayerIntent.SetIsScrubbing(false))
                    onPlayerIntent(PlayerIntent.SeekTo(it))
                    scrubPosition = null
                }
            )
        }
    }
}

@Composable
private fun ActionsRow(
    isCropped: Boolean,
    isControllerVisible: Boolean,
    onPlayerIntent: (PlayerIntent) -> Unit,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Side: Rewind and Fast Forward controls
        Row(horizontalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Outlined.RewindBack,
                onClick = { onPlayerIntent(PlayerIntent.SeekForFiveSeconds(false)) },
                isEnabled = isControllerVisible
            )

            PlayerIconButton(
                icon = LibertyFlowIcons.Outlined.Rewind,
                onClick = { onPlayerIntent(PlayerIntent.SeekForFiveSeconds(true)) },
                isEnabled = isControllerVisible
            )
        }

        // Right Side: Utility controls (Lock, Aspect Ratio, PiP)
        Row(horizontalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Outlined.Lock,
                onClick = { onPlayerIntent(PlayerIntent.ToggleIsLocked) },
                isEnabled = isControllerVisible
            )

            ButtonWithAnimatedIcon(
                icon = LibertyFlowIcons.Animated.Crop,
                atEnd = isCropped,
                onClick = { onPlayerIntent(PlayerIntent.ToggleCropped) }
            ) { painter ->
                Image(
                    painter = painter,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }

            PlayerIconButton(
                icon = LibertyFlowIcons.Outlined.Pip,
                onClick = {
                    onPlayerIntent(PlayerIntent.TurnOffController)
                    onPlayerEffect(PlayerEffect.TryPipEnterPip)
                },
                isEnabled = isControllerVisible
            )
        }
    }
}

/**
 * A custom seek bar that separates touch logic from visual rendering.
 *
 * It overlays a transparent [Slider] on top of a [LinearProgressIndicator].
 * This allows the use of Material 3's robust gesture handling while
 * maintaining a sleek, minimal progress line.
 *
 * @param currentPosition The actual playback position from the media engine.
 * @param totalDuration The length of the media in milliseconds.
 * @param scrubPosition The temporary position where the user is currently dragging.
 * @param onScrubbing Callback triggered during the drag gesture.
 * @param onSeekFinished Callback triggered when the user releases the slider.
 */
@Composable
private fun PlayerSlider(
    currentPosition: Long,
    totalDuration: Long,
    scrubPosition: Long?,
    onScrubbing: (Long) -> Unit,
    onSeekFinished: (Long) -> Unit
) {
    // Coerce duration to prevent division by zero or negative ranges
    val safeTotalDuration = remember(totalDuration) {
        totalDuration.coerceAtLeast(FooterDefaults.SAFE_TOTAL_DURATION).toFloat()
    }
    val displayPosition = (scrubPosition ?: currentPosition).toFloat()

    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
        // Using a progress lambda to defer read and minimize recompositions
        LinearProgressIndicator(
            progress = { if (safeTotalDuration > 0) displayPosition / safeTotalDuration else 0f },
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            trackColor = Color.White.copy(alpha = FooterDefaults.TRACK_ALPHA),
            strokeCap = StrokeCap.Round
        )

        // Overlaying a transparent Slider to handle touch input while keeping custom LinearProgress look
        Slider(
            value = displayPosition,
            valueRange = 0f..safeTotalDuration,
            modifier = Modifier
                .fillMaxWidth()
                .height(FooterDefaults.SliderHeight),
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

private fun Long.formatMinSec(): String {
    val totalSeconds = this / FooterDefaults.MILLIS_TO_SECONDS
    val minutes = totalSeconds / FooterDefaults.SECONDS_IN_MINUTE
    val seconds = totalSeconds % FooterDefaults.SECONDS_IN_MINUTE
    return "%02d:%02d".format(minutes, seconds)
}