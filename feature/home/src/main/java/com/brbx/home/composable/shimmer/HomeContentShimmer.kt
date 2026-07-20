package com.brbx.home.composable.shimmer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brbx.common.composable.utils.animeItemsShimmer
import com.brbx.design_system.component.rainbow_button.RainbowButtonShimmer
import com.brbx.design_system.component.tile.TileShimmer
import com.brbx.design_system.container.AnimeItemsLazyVerticalGrid
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.mColors

@Composable
internal fun HomeContentShimmer(
    modifier: Modifier = Modifier
) {
    AnimeItemsLazyVerticalGrid(
        userScrollEnabled = false,
        modifier = modifier,
    ) {
        item(
            key = HomeShimmerKeys.RandomAnimeButton,
            span = { GridItemSpan(currentLineSpan = maxLineSpan) },
        ) {
            RainbowButtonShimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp) // TODO Fix hardcoded 3.dp
                    .brbxAnimateItem(scope = this)
            )
        }

        item(
            key = HomeShimmerKeys.Tile,
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

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentShimmerPreview() {
    val scheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    BrbxTheme(colorScheme = scheme) {
        Box(
            modifier = Modifier.background(mColors.background)
        ) {
            HomeContentShimmer()
        }
    }
}
