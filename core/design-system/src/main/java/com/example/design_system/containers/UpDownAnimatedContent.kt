package com.example.design_system.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val ANIMATION_DURATION = 300
private const val HEIGHT_DIVIDER = 2

private const val ANIMATED_CONTENT_LABEL = "Animated up down content"

@Composable
fun <S> UpDownAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: S,
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        label = ANIMATED_CONTENT_LABEL,
        content = content,
        targetState = targetState,
        transitionSpec = { (
                slideInVertically(
                    animationSpec = tween(ANIMATION_DURATION)
                ) { it / HEIGHT_DIVIDER } + fadeIn(tween(ANIMATION_DURATION))
        ).togetherWith(
                exit = slideOutVertically(animationSpec = tween(ANIMATION_DURATION)
                ) { -it / HEIGHT_DIVIDER } + fadeOut(tween(ANIMATION_DURATION)))
            .using(SizeTransform(clip = false))
        }
    )
}