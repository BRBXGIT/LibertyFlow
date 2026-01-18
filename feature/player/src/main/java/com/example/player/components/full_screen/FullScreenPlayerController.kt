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
import androidx.compose.ui.zIndex
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mTypography
import com.example.player.R
import com.example.player.components.common.AnimatedPlayPauseButton
import com.example.player.components.common.rememberControllerVisibility
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState

// --- Constants: Dimensions ---
private const val MAIN_BOX_Z_INDEX = 1f
private const val INDEX_OFFSET = 1
private val SCREEN_EDGE_PADDING = 32.dp
private val HEADER_SPACING = 4.dp
private val COMMON_ICON_SPACING = 4.dp
private val CENTER_CONTROLS_SPACING = 36.dp

private const val HEADER_TEXT_MAX_LINES = 1

// --- Constants: Icon Sizes ---
private val SKIP_ICON_SIZE = 30.dp
private val PLAY_PAUSE_ICON_SIZE = 34.dp
private val SKIP_BUTTON_SIZE = 38.dp
private val PLAY_PAUSE_BUTTON_SIZE = 40.dp

// --- Constants: Resources ---
private val EpisodeLabel = R.string.episode_label
private val NoTitleProvidedLabel = R.string.no_title_provided_label

@Composable
internal fun FullScreenPlayerController(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val visibility = rememberControllerVisibility(playerState.isControllerVisible)

    // Extracting data here ensures sub-components only recompose when their specific data changes
    val currentEpisode = playerState.episodes.getOrNull(playerState.currentEpisodeIndex)
    val title = currentEpisode?.name ?: stringResource(NoTitleProvidedLabel)
    val episodeNumber = playerState.currentEpisodeIndex + INDEX_OFFSET

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(MAIN_BOX_Z_INDEX)
            .background(Color.Black.copy(alpha = visibility.tint))
            .graphicsLayer { alpha = visibility.alpha }
            .clickable(
                onClick = { onPlayerEffect(PlayerEffect.ToggleControllerVisible) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(
                top = systemBarsPadding.calculateTopPadding() + SCREEN_EDGE_PADDING,
                bottom = systemBarsPadding.calculateBottomPadding() + SCREEN_EDGE_PADDING,
                start = SCREEN_EDGE_PADDING,
                end = SCREEN_EDGE_PADDING
            )
    ) {
        Header(
            title = title,
            episodeNumber = episodeNumber,
            onPlayerEffect = onPlayerEffect
        )

        CenterControls(
            onEffect = onPlayerEffect,
            playerState = playerState
        )

        Footer(playerState, onPlayerEffect)
    }
}

// --- Reusable Components ---

@Composable
private fun PlayerIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = Dp.Unspecified,
    tint: Color = Color.White,
    isEnabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = isEnabled
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = if (iconSize != Dp.Unspecified) Modifier.size(iconSize) else Modifier,
            tint = tint
        )
    }
}

// --- Sub-sections ---

@Composable
private fun BoxScope.Header(
    title: String,
    episodeNumber: Int,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(COMMON_ICON_SPACING)
        ) {
            PlayerIconButton(icon = LibertyFlowIcons.ArrowDown, onClick = {})

            Column(verticalArrangement = Arrangement.spacedBy(HEADER_SPACING)) {
                Text(
                    text = title,
                    style = mTypography.bodyLarge.copy(
                        fontWeight = FontWeight.W600,
                        color = Color.White
                    ),
                    maxLines = HEADER_TEXT_MAX_LINES
                )
                Text(
                    text = "${stringResource(EpisodeLabel)} $episodeNumber",
                    style = mTypography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(COMMON_ICON_SPACING)) {
            PlayerIconButton(icon = LibertyFlowIcons.Checklist, onClick = {})
            PlayerIconButton(icon = LibertyFlowIcons.Settings, onClick = {})
        }
    }
}

@Composable
private fun BoxScope.CenterControls(
    playerState: PlayerState,
    onEffect: (PlayerEffect) -> Unit
) {
    Row(
        modifier = Modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CENTER_CONTROLS_SPACING)
    ) {
        // Backward Button
        PlayerIconButton(
            icon = LibertyFlowIcons.Previous,
            iconSize = SKIP_ICON_SIZE,
            modifier = Modifier.size(SKIP_BUTTON_SIZE),
            isEnabled = playerState.isControllerVisible,
            onClick = { onEffect(PlayerEffect.SkipEpisode(forward = false)) }
        )

        // Main Play/Pause Button
        AnimatedPlayPauseButton(
            playerState = playerState,
            onPlayerEffect = onEffect,
            iconSize = PLAY_PAUSE_ICON_SIZE,
            buttonSize = PLAY_PAUSE_BUTTON_SIZE
        )

        // Forward Button
        PlayerIconButton(
            icon = LibertyFlowIcons.Next,
            iconSize = SKIP_ICON_SIZE,
            modifier = Modifier.size(SKIP_BUTTON_SIZE),
            isEnabled = playerState.isControllerVisible,
            onClick = { onEffect(PlayerEffect.SkipEpisode(forward = true)) }
        )
    }
}

@Composable
private fun BoxScope.Footer(
    playerState: PlayerState,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.spacedBy(HEADER_SPACING)
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

            Row(horizontalArrangement = Arrangement.spacedBy(COMMON_ICON_SPACING)) {
                PlayerIconButton(icon = LibertyFlowIcons.Lock, onClick = {})

                // Animated Play/Pause icon handling
                val animatedVector = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.CropAnimated)
                val painter = rememberAnimatedVectorPainter(
                    animatedImageVector = animatedVector,
                    atEnd = !playerState.isCropped
                )

                IconButton(
                    onClick = { if (playerState.isControllerVisible) onPlayerEffect(PlayerEffect.ToggleCropped) }
                ) {
                    Image(
                        contentDescription = null,
                        painter = painter,
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }

                PlayerIconButton(icon = LibertyFlowIcons.Pip, onClick = {})
                PlayerIconButton(icon = LibertyFlowIcons.QuitFullScreen, onClick = {})
            }
        }

        // Use lambda-based progress for performance (Compose 1.5+)
        // This avoids recomposing the whole Footer when only progress changes
        LinearProgressIndicator(
            progress = { 0.5f },
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.24f)
        )
    }
}