package com.brbx.design_system.component.anime_card

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.coil_helpers.helpers.BrbxRemoteImage
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCard
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCardAppearance
import com.brbx.ui_compose.theme.bAnimationTokens

@Composable
fun AnimeCard(
    model: AnimeCardModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    appearance: BrbxContentCardAppearance = AnimeCardConstants.AnimeCardAppearance,
) =
    BrbxContentCard(
        modifier = modifier
            .size(AnimeCardConstants.Width, AnimeCardConstants.Height)
            .padding(all = appearance.containerElevation()),
        appearance = appearance,
        onClick = onClick,
        title = model.name,
        description = model.genresAsString().toBrbxText(),
        backgroundContent = {
            BrbxRemoteImage(
                model = model.fullPosterPath,
                crossfadeDuration = bAnimationTokens.short2.toInt(),
                modifier = Modifier.fillMaxSize(),
            )
        }
    )