package com.brbx.home.composable.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.composable.utils.animeItems
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Name
import com.brbx.common.model.common.model.Poster
import com.brbx.common.view_model.processor.tile.model.CommonTile
import com.brbx.common.view_model.processor.tile.model.TileType
import com.brbx.design_system.component.rainbow_button.RainbowButton
import com.brbx.design_system.component.tile.Tile
import com.brbx.design_system.container.AnimeItemsLazyVerticalGrid
import com.brbx.design_system.container.PullToRefreshContainer
import com.brbx.home.common.HomeStrings
import com.brbx.home.view_model.model.Intent
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.BrbxTheme
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.FacesEmotionsStickers
import dev.chiksmedina.solar.outline.facesemotionsstickers.EmojiFunnySquare
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun Content(
    animeGridState: LazyGridState,
    tile: CommonTile?,
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
            userScrollEnabled = !isRefreshing,
        ) {
            if (!isSearching) {
                randomAnimeButton(
                    isLoading = isRandomAnimeLoading,
                    onClick = { dispatchIntent(Intent.GetRandomAnime) }
                )

                tileItem(tile = tile)
            }

            animeItems(
                items = items,
                onItemClick = { /* TODO Navigate to details */ },
                selectedIds = emptySet(),
                onItemLongClick = { /* TODO Toggle selected ids set */ },
            )
        }
    }
}

private fun LazyGridScope.randomAnimeButton(
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    item(
        key = ContentKeys.RandomAnimeButtonKey,
        span = { GridItemSpan(currentLineSpan = maxLineSpan) },
    ) {
        RainbowButton(
            text = HomeStrings.random_anime_button_text.toBrbxText(),
            showAnimation = isLoading,
            icon = OutlineSolar.FacesEmotionsStickers.EmojiFunnySquare.toBrbxIcon(),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .brbxAnimateItem(scope = this)
        )
    }
}

private fun LazyGridScope.tileItem(tile: CommonTile?) {
    if (tile == null) return
    item(
        key = ContentKeys.Tile,
        span = { GridItemSpan(currentLineSpan = maxLineSpan) },
    ) {
        val onTileClick: () -> Unit = remember(tile) { {} }

        Tile(
            title = tile.title,
            description = tile.description,
            icon = tile.icon,
            onTileClick = onTileClick,
            isPrecollectionVisible = tile.isPrecollectionVisible,
            precollectionIcon = tile.precollection?.icon,
            precollectionText = tile.precollection?.label,
            onPrecollectionClick = onTileClick,
            modifier = Modifier.brbxAnimateItem(scope = this)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {
    val mockItems = flowOf(
        PagingData.from(
            listOf(
                AnimeItem(
                    id = 1,
                    name = Name(russian = "Атака титанов", english = "Attack on Titan", alternative = null),
                    genres = listOf(Genre(id = 1, name = "Экшен")),
                    posterPath = Poster(preview = "", src = "", thumbnail = "")
                ),
                AnimeItem(
                    id = 2,
                    name = Name(russian = "Наруто", english = "Naruto", alternative = null),
                    genres = listOf(Genre(id = 2, name = "Приключения")),
                    posterPath = Poster(preview = "", src = "", thumbnail = "")
                )
            )
        )
    ).collectAsLazyPagingItems()

    BrbxTheme(colorScheme = lightColorScheme()) {
        Content(
            animeGridState = rememberLazyGridState(),
            tile = CommonTile(
                type = TileType.Episode.LatestWatched,
                title = "Продолжить просмотр".toBrbxText(),
                description = "Серия 12".toBrbxText(),
                icon = OutlineSolar.FacesEmotionsStickers.EmojiFunnySquare.toBrbxIcon(),
                precollection = null,
                isPrecollectionVisible = false,
            ),
            items = mockItems,
            isRefreshing = false,
            isSearching = false,
            isRandomAnimeLoading = false,
            dispatchIntent = {}
        )
    }
}
