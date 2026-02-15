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
import com.example.design_system.theme.theme.mAnimationTokens


/**
 * A custom [AnimatedContent] wrapper that provides a vertically sliding "Roll" transition.
 * * When the [targetState] changes, the new content slides in from the bottom while
 * the old content slides out through the top. Both movements are accompanied
 * by a fade transition and use half-height offsets to create a smooth,
 * overlapping motion.
 *
 *
 *
 * @param S The type of the state used to trigger the animation.
 * @param modifier [Modifier] to be applied to the animation container.
 * @param targetState The current state to display. When this changes, the transition triggers.
 * @param content The Composable content to be displayed for the provided [targetState].
 */
@Composable
fun <S> DownUpAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: S,
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit
) {
    val animationTokens = mAnimationTokens

    AnimatedContent(
        modifier = modifier,
        label = "Animated down up content",
        content = content,
        targetState = targetState,
        transitionSpec = { (
                slideInVertically(
                    animationSpec = tween(animationTokens.short)
                ) { it / 2 } + fadeIn(tween(animationTokens.short))
        ).togetherWith(
                exit = slideOutVertically(animationSpec = tween(animationTokens.short)
                ) { -it / 2 } + fadeOut(tween(animationTokens.short)))
            .using(SizeTransform(clip = false))
        }
    )
}