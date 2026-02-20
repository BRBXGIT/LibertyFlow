package com.example.design_system.components.icons

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter

@Composable
fun LibertyFlowAnimatedIcon(
    colorFilter: ColorFilter,
    isRunning: Boolean,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    val animatedVector = AnimatedImageVector.animatedVectorResource(iconRes)
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = animatedVector,
        atEnd = isRunning
    )

    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = null,
        colorFilter = colorFilter
    )
}