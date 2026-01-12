@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.player.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.media3.exoplayer.ExoPlayer
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerState

private const val ANIMATION_DURATION = 500

private const val ANIMATED_CONTENT_LABEL = "PlayerTransition"

internal const val PLAYER_SHARED_ELEMENT_KEY = "player_shared_element_key"

@Composable
fun SharedTransitionScope.PlayerContainer(
    playerState: PlayerState,
    player: ExoPlayer,
    navBarVisible: Boolean,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    // TODO: maybe need to delete shared transition
    AnimatedContent(
        targetState = playerState.playerState,
        transitionSpec = {
            fadeIn(tween(ANIMATION_DURATION)) togetherWith fadeOut(tween(ANIMATION_DURATION))
        },
        label = ANIMATED_CONTENT_LABEL
    ) { targetState ->
        when (targetState) {
            PlayerState.PlayerState.Full -> {
                FullScreenPlayerContainer(
                    player = player,
                    playerState = playerState,
                    onPlayerEffect = onPlayerEffect,
                    sharedTransitionScope = this@PlayerContainer,
                    animatedVisibilityScope = this@AnimatedContent
                )
            }

            PlayerState.PlayerState.Mini -> {
                MiniPlayerContainer(
                    player = player,
                    navBarVisible = navBarVisible,
                    playerState = playerState,
                    onPlayerEffect = onPlayerEffect,
                    sharedTransitionScope = this@PlayerContainer,
                    animatedVisibilityScope = this@AnimatedContent
                )
            }

            PlayerState.PlayerState.Closed -> {}
        }
    }
}