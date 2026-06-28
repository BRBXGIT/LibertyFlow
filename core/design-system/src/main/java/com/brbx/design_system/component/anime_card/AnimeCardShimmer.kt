package com.brbx.design_system.component.anime_card

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brbx.ui_compose.components.complex.content_card.content_card.BrbxContentCardAppearance
import com.brbx.ui_compose.components.complex.content_card.shimmer.BrbxContentCardShimmer
import com.brbx.ui_compose.theme.BrbxTheme

@Composable
fun AnimeCardShimmer(
    modifier: Modifier = Modifier,
    appearance: BrbxContentCardAppearance = AnimeCardConstants.animeCardAppearance,
) =
    BrbxContentCardShimmer(
        modifier = modifier,
        appearance = appearance,
    )

@Preview
@Composable
private fun AnimeCardShimmerPreview() {
    BrbxTheme(lightColorScheme()) {
        AnimeCardShimmer()
    }
}