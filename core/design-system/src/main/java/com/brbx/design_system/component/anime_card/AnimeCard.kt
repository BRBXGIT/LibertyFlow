package com.brbx.design_system.component.anime_card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.brbx.coil_helpers.helpers.BrbxRemoteImage
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCard
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCardAppearance
import com.brbx.ui_compose.components.complex.content_card.content_card.rememberCopy
import com.brbx.ui_compose.theme.bAnimationTokens
import com.brbx.ui_compose.theme.bMotion

@Composable
fun AnimeCard(
    posterPath: String?,
    description: BrbxText,
    title: BrbxText,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    selected: Boolean = false,
    appearance: BrbxContentCardAppearance = AnimeCardConstants.AnimeCardAppearance,
) {
    val selectedColor = AnimeCardConstants.selectedBorderColor
    val animatedBorderColor by animateColorAsState(
        targetValue = if (selected) selectedColor else Color.Transparent,
        animationSpec = bMotion.nonSpatialFastSpec(),
        label = "Selected border color animation"
    )
    val animatedBorderStroke by animateDpAsState(
        targetValue = if (selected) 3.dp else 0.dp,
        animationSpec = bMotion.mediumSpatialSpec(),
        label = "Selected border width animation"
    )
    val finalAppearance = appearance.rememberCopy(
        containerBorder = {
            BorderStroke(width = animatedBorderStroke, color = animatedBorderColor)
        }
    )
    BrbxContentCard(
        modifier = modifier
            .size(AnimeCardConstants.Width, AnimeCardConstants.Height)
            .padding(all = appearance.containerElevation()),
        appearance = finalAppearance,
        onClick = onClick,
        title = title,
        description = description,
        onLongClick = onLongClick,
        backgroundContent = {
            BrbxRemoteImage(
                model = posterPath,
                crossfadeDuration = bAnimationTokens.short2.toInt(),
                modifier = Modifier.fillMaxSize(),
            )
        }
    )
}