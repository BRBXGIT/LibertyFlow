@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.full_screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.design_system.components.buttons.ButtonWithIcon
import com.example.design_system.components.buttons.ButtonWithIconType
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mTypography
import com.example.player.R
import com.example.player.components.common.AnimatedPlayPauseButton
import com.example.player.components.common.ControllerVisibility
import com.example.player.components.common.rememberControllerVisibility
import com.example.player.player.PlayerEffect
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

@Composable
internal fun FullScreenPlayerController(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    if (playerState.isEpisodesDialogVisible) {
        EpisodeDialog(onPlayerEffect, playerState)
    }

    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val visibility = rememberControllerVisibility(playerState.isControllerVisible)

    // Derived data for UI
    val currentEpisode = playerState.episodes.getOrNull(playerState.currentEpisodeIndex)
    val title = currentEpisode?.name ?: stringResource(NoTitleLabel)
    val episodeNumber = playerState.currentEpisodeIndex + 1

    // 1. Interaction Layer (Captures taps to toggle visibility)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = { onPlayerEffect(PlayerEffect.ToggleControllerVisible) },
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
            contentPadding = systemBarsPadding
        )
    }

    // 3. Unlock Overlay (Only visible when locked)
    if (playerState.isLocked) {
        UnlockOverlay(
            onPlayerEffect = onPlayerEffect,
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
    onPlayerEffect: (PlayerEffect) -> Unit,
    contentPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { this.alpha = visibility.overlayAlpha }
            .background(Color.Black.copy(alpha = visibility.controlsAlpha))
            .padding(
                top = contentPadding.calculateTopPadding() + EdgePadding,
                bottom = contentPadding.calculateBottomPadding() + EdgePadding,
                start = EdgePadding,
                end = EdgePadding
            )
    ) {
        Header(title, episodeNumber, playerState, onPlayerEffect)
        CenterControls(playerState, onPlayerEffect)
        Footer(playerState, onPlayerEffect)
    }
}

private val EpisodeLabel = R.string.episode_label

@Composable
private fun BoxScope.Header(
    title: String,
    episodeNumber: Int,
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IconSpacing)
        ) {
            PlayerIconButton(
                icon = LibertyFlowIcons.ArrowDown,
                onClick = { onPlayerEffect(PlayerEffect.ToggleFullScreen) },
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
                onClick = { onPlayerEffect(PlayerEffect.ToggleEpisodesDialog) },
                isEnabled = playerState.isControllerVisible
            )
            PlayerIconButton(
                icon = LibertyFlowIcons.Settings,
                onClick = {},
                isEnabled = playerState.isControllerVisible
            )
        }
    }
}

@Composable
private fun BoxScope.CenterControls(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    Row(
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ControlSpacing)
    ) {
        PlayerIconButton(
            icon = LibertyFlowIcons.Previous,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipButtonSize),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = false)) },
            isEnabled = playerState.isControllerVisible
        )

        AnimatedPlayPauseButton(
            playerState = playerState,
            onPlayerEffect = onPlayerEffect,
            iconSize = PlayPauseIconSize,
            buttonSize = PlayPauseButtonSize,
            isEnabled = playerState.isControllerVisible
        )

        PlayerIconButton(
            icon = LibertyFlowIcons.Next,
            iconSize = SkipIconSize,
            modifier = Modifier.size(SkipButtonSize),
            onClick = { onPlayerEffect(PlayerEffect.SkipEpisode(forward = true)) },
            isEnabled = playerState.isControllerVisible
        )
    }
}

private const val TRACK_ALPHA = 0.24f

@Composable
private fun BoxScope.Footer(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.spacedBy(HeaderSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "00:12 / 23:58",
                style = mTypography.bodyMedium.copy(color = Color.White)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(IconSpacing)) {
                PlayerIconButton(
                    icon = LibertyFlowIcons.Lock,
                    onClick = { onPlayerEffect(PlayerEffect.ToggleIsLocked) },
                    isEnabled = playerState.isControllerVisible
                )

                // Animated Crop Toggle
                val animatedVector = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.CropAnimated)
                val painter = rememberAnimatedVectorPainter(animatedVector, !playerState.isCropped)
                IconButton(onClick = { onPlayerEffect(PlayerEffect.ToggleCropped) }) {
                    Image(painter = painter, contentDescription = null, colorFilter = ColorFilter.tint(Color.White))
                }

                PlayerIconButton(
                    icon = LibertyFlowIcons.Pip,
                    onClick = { /* Handle PiP */ },
                    isEnabled = playerState.isControllerVisible
                )
                PlayerIconButton(
                    icon = LibertyFlowIcons.QuitFullScreen,
                    onClick = { onPlayerEffect(PlayerEffect.ToggleFullScreen) },
                    isEnabled = playerState.isControllerVisible
                )
            }
        }

        LinearProgressIndicator(
            progress = { 0.5f }, // Use lambda for performance
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            trackColor = Color.White.copy(alpha = TRACK_ALPHA)
        )
    }
}

private val UnlockLabel = R.string.unlock_label

@Composable
private fun UnlockOverlay(
    onPlayerEffect: (PlayerEffect) -> Unit,
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
            onClick = { onPlayerEffect(PlayerEffect.ToggleIsLocked) },
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
    isEnabled: Boolean = true
) {
    IconButton(onClick = onClick, modifier = modifier, enabled = isEnabled) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = if (iconSize != Dp.Unspecified) Modifier.size(iconSize) else Modifier,
            tint = Color.White
        )
    }
}