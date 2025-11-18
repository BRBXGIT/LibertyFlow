package com.example.design_system.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.data.models.common.common.PosterType
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.cards.AnimeCard
import com.example.design_system.components.cards.AnimeCardUtils

internal object AnimeLVGContainerUtils {
    const val ARRANGEMENT_ALIGNMENT_PADDING = 16
}

@Composable
fun AnimeItemsLazyVerticalGrid(
    anime: List<UiAnimeItem>
) {
    AnimeLazyVerticalGridContainer {
        items(anime, key = { it.id }) { anime ->
            AnimeCard(
                posterPath = anime.poster.fullPath(PosterType.PREVIEW),
                genresString = anime.genresAsString(),
                title = anime.name.russian,
                onCardClick = {}
            )
        }
    }
}

@Composable
fun PagingAnimeItemsLazyVerticalGrid(
    anime: LazyPagingItems<UiAnimeItem>
) {
    AnimeLazyVerticalGridContainer {
        items(anime.itemCount) { index ->
            val current = anime[index]

            current?.let {
                AnimeCard(
                    posterPath = current.poster.fullPath(PosterType.PREVIEW),
                    genresString = current.genresAsString(),
                    title = current.name.russian,
                    onCardClick = {}
                )
            }
        }
    }
}

@Composable
private fun AnimeLazyVerticalGridContainer(
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(AnimeLVGContainerUtils.ARRANGEMENT_ALIGNMENT_PADDING.dp),
        horizontalArrangement = Arrangement.spacedBy(AnimeLVGContainerUtils.ARRANGEMENT_ALIGNMENT_PADDING.dp),
        columns = GridCells.Adaptive(AnimeCardUtils.CARD_WIDTH.dp),
        contentPadding = PaddingValues(AnimeLVGContainerUtils.ARRANGEMENT_ALIGNMENT_PADDING.dp),
        content = content,
        modifier = Modifier.fillMaxSize()
    )
}