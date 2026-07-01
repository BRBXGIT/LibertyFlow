package com.brbx.home.screen.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brbx.design_system.component.rainbow_button.RainbowButtonShimmer
import com.brbx.design_system.component.tile.TileShimmer
import com.brbx.design_system.container.AnimeItemsLazyVerticalGrid
import com.brbx.design_system.container.animeItemsShimmer
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.mColors

@Composable
internal fun ContentShimmer(
    modifier: Modifier = Modifier
) {
    AnimeItemsLazyVerticalGrid(
        userScrollEnabled = false,
        modifier = modifier,
    ) {
        item(
            key = ShimmerKeys.RandomAnimeButton,
            span = { GridItemSpan(currentLineSpan = maxLineSpan) },
        ) {
            RainbowButtonShimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .brbxAnimateItem(scope = this)
            )
        }

        item(
            key = ShimmerKeys.Tile,
            span = { GridItemSpan(currentLineSpan = maxLineSpan) },
        ) {
            TileShimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .brbxAnimateItem(scope = this)
            )
        }

        animeItemsShimmer()
    }
}

@Preview
@Composable
private fun ContentShimmerPreview() {
    BrbxTheme(lightColorScheme()) {
        Box(
            modifier = Modifier.background(mColors.background)
        ) {
            ContentShimmer()
        }
    }
}