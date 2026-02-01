@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.full_screen

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.design_system.components.buttons.ButtonWithIcon
import com.example.design_system.components.buttons.ButtonWithIconType
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mTypography
import com.example.player.R
import com.example.player.components.common.AnimatedPlayPauseButton
import com.example.player.components.common.ControllerVisibility
import com.example.player.components.common.rememberControllerVisibility
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

// --- Constants & Configuration ---
private val EdgePadding = 32.dp
private val HeaderSpacing = 4.dp
private val ControlSpacing = 36.dp
private val IconSpacing = 4.dp

private val SkipIconSize = 30.dp
private val PlayPauseIconSize = 34.dp
private val SkipButtonSize = 38.dp
private val PlayPauseButtonSize = 40.dp

private val NoTitleLabel = R.string.no_title_provided_label

private const val ZERO = 0
private const val ONE = 1

@Composable
internal fun FullScreenPlayerController(
    playerState: PlayerState,
    pipManager: PipManager,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val visibility = rememberControllerVisibility(playerState.isControllerVisible, playerState.episodeTime.isScrubbing)

    // Derived data for UI
    val currentEpisode = playerState.episodes.getOrNull(playerState.currentEpisodeIndex)
    val title = currentEpisode?.name ?: stringResource(NoTitleLabel)
    val episodeNumber = playerState.currentEpisodeIndex + ONE

    // 1. Interaction Layer (Captures taps to toggle visibility)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = { onPlayerIntent(PlayerIntent.ToggleControllerVisible) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    )

    // 2. Main Controls (Only visible when NOT locked)
    if (!playerState.isLocked) {
        MainControlsOverlay(
            title = title,
            episodeNumber = episodeNumber,
            playerState = playerState,
            onPlayerEffect = onPlayerEffect,
            visibility = visibility,
            contentPadding = systemBarsPadding,
            pipManager = pipManager,
            onPlayerIntent = onPlayerIntent
        )
    }

    // 3. Unlock Overlay (Only visible when locked)
    if (playerState.isLocked) {
        UnlockOverlay(
            onPlayerIntent = onPlayerIntent,
            alpha = visibility.controlsAlpha
        )
    }
}

@Composable
private fun MainControlsOverlay(
    title: String,
    episodeNumber: Int,
    playerState: PlayerState,
    visibility: ControllerVisibility,
    pipManager: PipManager,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit,
    contentPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = visibility.controlsAlpha }
            .background(Color.Black.copy(alpha = visibility.overlayAlpha))
            .padding(
                top = contentPadding.calculateTopPadding() + EdgePadding,
                bottom = contentPadding.calculateBottomPadding() + EdgePadding,
                start = EdgePadding,
                end = EdgePadding
            )
    ) {
        Header(title, episodeNumber, playerState, onPlayerIntent)
        CenterControls(playerState, onPlayerEffect, onPlayerIntent)
        Footer(playerState, pipManager, onPlayerEffect, onPlayerIntent)
        SkipOpeningButton(playerState.isSkipOpeningButtonVisible, onPlayerIntent)
    }
}

private val EpisodeLabel = R.string.episode_label

@Composable
private fun BoxScope.Header(
    title: String,
    episodeNumber: Int,
    playerState: PlayerState,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IconSpacing)
        ) {
            PlayerIconButton(
                icon = LibertyFlowIcons.ArrowDown,
                onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) },
                isEnabled = playerState.isControllerVisible
            )
            Column(verticalArrangement = Arrangement.spacedBy(HeaderSpacing)) {
                Text(
                    text = title,
                    style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600, color = Color.White),
                    maxLines = 1
                )
                Text(
                    text = "${stringResource(EpisodeLabel)} $episodeNumber",
                    style = mTypography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(IconSpacing)) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Checklist,
                onClick = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) },
                isEnabled = playerState.isControllerVisible
            )
            PlayerIconButton(
                icon = LibertyFlowIcons.Settings,
                onClick = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) },
                isEnabled = playerState.isControllerVisible
            )
        }
    }
}

@Composable
private fun BoxScope.CenterControls(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Row(
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ControlSpacing)
    ) {
        PlayerIconButton(
            isAvailable = playerState.currentEpisodeIndex > ZERO,
            icon = LibertyFlowIcons.Previous,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipButtonSize),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = false)) },
            isEnabled = playerState.isControllerVisible
        )

        AnimatedPlayPauseButton(
            playerState = playerState,
            onPlayerIntent = onPlayerIntent,
            iconSize = PlayPauseIconSize,
            buttonSize = PlayPauseButtonSize,
            isEnabled = playerState.isControllerVisible
        )

        PlayerIconButton(
            isAvailable = playerState.currentEpisodeIndex < playerState.episodes.size - ONE,
            icon = LibertyFlowIcons.Next,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipButtonSize),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = true)) },
            isEnabled = playerState.isControllerVisible
        )
    }
}

private const val SLASH = "/"

@Composable
private fun BoxScope.Footer(
    playerState: PlayerState,
    pipManager: PipManager,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    var scrubPosition by remember { mutableStateOf<Long?>(null) }

    val currentTime = scrubPosition ?: playerState.episodeTime.current
    val totalTime = playerState.episodeTime.total

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.spacedBy(HeaderSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${currentTime.formatMinSec()} $SLASH ${totalTime.formatMinSec()}",
                style = mTypography.bodyMedium.copy(color = Color.White)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(IconSpacing)) {
                PlayerIconButton(
                    icon = LibertyFlowIcons.Lock,
                    onClick = { onPlayerIntent(PlayerIntent.ToggleIsLocked) },
                    isEnabled = playerState.isControllerVisible
                )

                // Animated Crop Toggle
                val animatedVector = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.CropAnimated)
                val painter = rememberAnimatedVectorPainter(animatedVector, !playerState.isCropped)
                IconButton(
                    onClick = { onPlayerIntent(PlayerIntent.ToggleCropped) }
                ) {
                    Image(painter = painter, contentDescription = null, colorFilter = ColorFilter.tint(Color.White))
                }

                val pipContext = LocalContext.current
                PlayerIconButton(
                    icon = LibertyFlowIcons.Pip,
                    onClick = {
                        onPlayerIntent(PlayerIntent.TurnOffController)
                        if (pipManager.isPipSupported(pipContext)) {
                            pipManager.updatedPipParams()?.let { params ->
                                (pipContext as Activity).enterPictureInPictureMode(params)
                            }
                        }
                    },
                    isEnabled = playerState.isControllerVisible
                )
                PlayerIconButton(
                    icon = LibertyFlowIcons.QuitFullScreen,
                    onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) },
                    isEnabled = playerState.isControllerVisible
                )
            }
        }

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

private const val TRACK_ALPHA = 0.24f
private const val SAFE_RANGE_START = 0f

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
    val displayPosition = (scrubPosition ?: currentPosition).toFloat()
    val safeTotalDuration = totalDuration.coerceAtLeast(SAFE_TOTAL_DURATION).toFloat()

    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = { displayPosition / safeTotalDuration },
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            trackColor = Color.White.copy(alpha = TRACK_ALPHA),
            strokeCap = StrokeCap.Round
        )

        Slider(
            value = displayPosition,
            valueRange = SAFE_RANGE_START..safeTotalDuration,
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

private const val TOTAL_SECONDS_DIVIDER = 1000
private const val SECONDS_MINUTES_DIVIDER = 60
private const val FORMAT = "%02d:%02d"

private fun Long.formatMinSec(): String {
    val totalSeconds = this / TOTAL_SECONDS_DIVIDER
    val minutes = totalSeconds / SECONDS_MINUTES_DIVIDER
    val seconds = totalSeconds % SECONDS_MINUTES_DIVIDER
    return FORMAT.format(minutes, seconds)
}

private val SkipOpeningLabel = R.string.skip_opening_label
private const val BUTTON_ANIMATION_LABEL = "Skip opening button animation label"

@Composable
private fun BoxScope.SkipOpeningButton(visible: Boolean, onPlayerIntent: (PlayerIntent) -> Unit) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = BUTTON_ANIMATION_LABEL
    )

    ButtonWithIcon(
        text = stringResource(SkipOpeningLabel),
        icon = LibertyFlowIcons.RewindForwardCircle,
        type = ButtonWithIconType.Outlined,
        onClick = { onPlayerIntent(PlayerIntent.SkipOpening) },
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .graphicsLayer { alpha = animatedAlpha },
    )
}

private val UnlockLabel = R.string.unlock_label

@Composable
private fun UnlockOverlay(
    onPlayerIntent: (PlayerIntent) -> Unit,
    alpha: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { this.alpha = alpha }
            .padding(bottom = EdgePadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        ButtonWithIcon(
            text = stringResource(UnlockLabel),
            icon = LibertyFlowIcons.Unlock,
            onClick = { onPlayerIntent(PlayerIntent.ToggleIsLocked) },
            type = ButtonWithIconType.Outlined
        )
    }
}

@Composable
private fun PlayerIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = Dp.Unspecified,
    isEnabled: Boolean = true,
    isAvailable: Boolean = true
) {
    IconButton(
        onClick = { if (isAvailable) onClick() },
        modifier = modifier,
        enabled = isEnabled
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = if (iconSize != Dp.Unspecified) Modifier.size(iconSize) else Modifier,
            tint = if (isAvailable) Color.White else Color.Gray
        )
    }
}