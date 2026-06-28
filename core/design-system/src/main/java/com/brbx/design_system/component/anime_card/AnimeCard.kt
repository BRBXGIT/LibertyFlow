package com.brbx.design_system.component.anime_card

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brbx.coil_helpers.helpers.BrbxRemoteImage
import com.brbx.design_system.common.DesignConstants
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCard
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCardAppearance
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.bAnimationTokens

@Composable
fun AnimeCard(
    title: BrbxText,
    description: BrbxText,
    posterPath: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    appearance: BrbxContentCardAppearance = AnimeCardConstants.animeCardAppearance,
) =
    BrbxContentCard(
        modifier = modifier.padding(all = DesignConstants.elevation),
        appearance = appearance,
        onClick = onClick,
        title = title,
        description = description,
        backgroundContent = {
            BrbxRemoteImage(
                model = posterPath,
                crossfadeDuration = bAnimationTokens.short2.toInt(),
                modifier = Modifier.fillMaxSize(),
            )
        }
    )

@Preview
@Composable
private fun AnimeCardPreview() {
    BrbxTheme(colorScheme = lightColorScheme()) {
        AnimeCard(
            title = "Blabla".toBrbxText(),
            description = "Long long blabla".toBrbxText(),
            onClick = {},
            posterPath = "",
        )
    }
}