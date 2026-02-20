@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

/**
 * A specialized button that renders and manages an [AnimatedImageVector].
 * * This component is ideal for toggleable actions (like Play/Pause or Mute/Unmute)
 * where the icon needs to animate between a start and end state based on the
 * current playback or UI status.
 *
 * @param icon The [AnimatedImageVector] resource ID (usually an XML <animated-vector>).
 * @param atEnd Controls the state of the animation. If true, the animation stays
 * at the end state; if false, it remains at the start state.
 * @param onClick Callback triggered when the button is tapped.
 * @param modifier Modifiers to be applied to the [IconButton].
 * @param isEnabled Whether the button is interactive.
 * @param content A slot-based lambda that receives the [Painter]. This allows the
 * caller to define how the icon is rendered (e.g., applying specific tints,
 * sizes, or [androidx.compose.foundation.Image] scaling).
 */
@Composable
fun ButtonWithAnimatedIcon(
    @DrawableRes icon: Int,
    atEnd: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    content: @Composable (Painter) -> Unit
) {
    val animatedVector = AnimatedImageVector.animatedVectorResource(icon)
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = animatedVector,
        atEnd = atEnd
    )

    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnabled
    ) {
        content(painter)
    }
}