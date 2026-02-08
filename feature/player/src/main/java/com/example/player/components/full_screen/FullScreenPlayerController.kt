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
private val EpisodeLabel = R.string.episode_label
private val SkipOpeningLabel = R.string.skip_opening_label
private val UnlockLabel = R.string.unlock_label

private const val ZERO = 0
private const val ONE = 1
private const val TRACK_ALPHA = 0.24f
private const val SAFE_TOTAL_DURATION = 0L
private val SliderHeight = 1.dp

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

@Composable
private fun BoxScope.Header(
    title: String,
    episodeNumber: Int,
    isControllerVisible: Boolean,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Side: Back & Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IconSpacing)
        ) {
            PlayerIconButton(
                icon = LibertyFlowIcons.ArrowDown,
                onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) },
                isEnabled = isControllerVisible
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

        // Right Side: Quick Settings
        Row(horizontalArrangement = Arrangement.spacedBy(IconSpacing)) {
            PlayerIconButton(
                icon = LibertyFlowIcons.Checklist,
                onClick = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) },
                isEnabled = isControllerVisible
            )
            PlayerIconButton(
                icon = LibertyFlowIcons.Settings,
                onClick = { onPlayerIntent(PlayerIntent.ToggleSettingsBS) },
                isEnabled = isControllerVisible
            )
        }
    }
}

@Composable
private fun BoxScope.CenterControls(
    currentEpisodeIndex: Int,
    totalEpisodes: Int,
    playerState: PlayerState,
    isControllerVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Row(
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ControlSpacing)
    ) {
        PlayerIconButton(
            isAvailable = currentEpisodeIndex > ZERO,
            icon = LibertyFlowIcons.Previous,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipButtonSize),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = false)) },
            isEnabled = isControllerVisible
        )

        AnimatedPlayPauseButton(
            playerState = playerState,
            onPlayerIntent = onPlayerIntent,
            iconSize = PlayPauseIconSize,
            buttonSize = PlayPauseButtonSize,
            isEnabled = isControllerVisible
        )

        PlayerIconButton(
            isAvailable = currentEpisodeIndex < totalEpisodes - ONE,
            icon = LibertyFlowIcons.Next,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipButtonSize),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = true)) },
            isEnabled = isControllerVisible
        )
    }
}

@Composable
private fun BoxScope.Footer(
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

            // Dynamic Crop Toggle with Vector Animation
            CropButton(isCropped = playerState.isCropped) {
                onPlayerIntent(PlayerIntent.ToggleCropped)
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

            PlayerIconButton(
                icon = LibertyFlowIcons.QuitFullScreen,
                onClick = { onPlayerIntent(PlayerIntent.ToggleFullScreen) },
                isEnabled = playerState.isControllerVisible
            )
        }
    }
}

@Composable
private fun CropButton(isCropped: Boolean, onClick: () -> Unit) {
    val animatedVector = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.CropAnimated)
    val painter = rememberAnimatedVectorPainter(animatedVector, !isCropped)
    IconButton(onClick = onClick) {
        Image(
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

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

@Composable
private fun BoxScope.SkipOpeningButton(visible: Boolean, onPlayerIntent: (PlayerIntent) -> Unit) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = "SkipOpeningAlpha"
    )

    if (animatedAlpha > 0f) {
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
}

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

// --- Utils ---
private fun Long.formatMinSec(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}