@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.common

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

@Composable
fun ButtonWithAnimatedIcon(
    iconId: Int,
    atEnd: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    content: @Composable (Painter) -> Unit
) {
    val animatedVector = AnimatedImageVector.animatedVectorResource(iconId)
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