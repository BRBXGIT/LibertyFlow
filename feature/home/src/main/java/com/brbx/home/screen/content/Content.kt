package com.brbx.home.screen.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.design_system.component.anime_card.AnimeCard
import com.brbx.design_system.component.rainbow_button.RainbowButton
import com.brbx.design_system.component.tile.Tile
import com.brbx.design_system.component.tile.TileModel
import com.brbx.design_system.container.AnimeItemsLazyVerticalGrid
import com.brbx.design_system.container.PullToRefreshContainer
import com.brbx.home.common.HomeStrings
import com.brbx.home.view_model.model.Intent
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.FacesEmotionsStickers
import dev.chiksmedina.solar.outline.facesemotionsstickers.EmojiFunnySquare

@Composable
internal fun Content(
    animeGridState: LazyGridState,
    tile: TileModel?,
    items: LazyPagingItems<AnimeItem>,
    isRefreshing: Boolean,
    isSearching: Boolean,
    isRandomAnimeLoading: Boolean,
    dispatchIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshContainer(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = items::refresh,
        minimalisticIndicator = isSearching,
    ) {
        AnimeItemsLazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = animeGridState,
        ) {
            if (!isSearching) {
                item(
                    key = ContentKeys.RandomAnimeButtonKey,
                    span = { GridItemSpan(currentLineSpan = maxLineSpan) },
                ) {
                    tile?.let {
                        RainbowButton(
                            text = HomeStrings.random_anime_button_text.toBrbxText(),
                            showAnimation = isRandomAnimeLoading,
                            icon = OutlineSolar.FacesEmotionsStickers.EmojiFunnySquare.toBrbxIcon(),
                            onClick = { dispatchIntent(Intent.GetRandomAnime) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .brbxAnimateItem(scope = this)
                        )
                    }
                }
            }

            if (!isSearching) {
                item(
                    key = ContentKeys.Tile,
                    span = { GridItemSpan(currentLineSpan = maxLineSpan) },
                ) {
                    tile?.let {
                        Tile(
                            model = tile,
                            modifier = Modifier
                                .fillMaxWidth()
                                .brbxAnimateItem(scope = this)
                        )
                    }
                }
            }

            items(
                count = items.itemCount,
                key = { index -> index }
            ) { index ->
                val current = items[index]
                current?.let { anime ->
                    AnimeCard(
                        modifier = Modifier.brbxAnimateItem(scope = this),
                        title = anime.name.russian.toBrbxText(),
                        description = anime.genresAsString().toBrbxText(),
                        posterPath = anime.poster.fullPreview(),
                        onClick = { /* TODO Navigate to details */ },
                    )
                }
            }
        }
    }
}