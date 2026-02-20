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