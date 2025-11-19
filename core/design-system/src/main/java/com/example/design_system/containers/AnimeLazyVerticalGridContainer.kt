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

private object AnimeLVGContainerUtils {
    const val ARRANGEMENT_ALIGNMENT_PADDING = 16
}

@Composable
fun AnimeItemsLazyVerticalGrid(
    anime: List<UiAnimeItem>
) {
    // Static list grid
    AnimeLazyVerticalGridContainer {
        items(
            items = anime,
            key = { it.id }
        ) { item ->
            AnimeCard(
                posterPath = item.poster.fullPath(PosterType.PREVIEW),
                genresString = item.genresAsString(),
                title = item.name.russian,
                onCardClick = {}
            )
        }
    }
}

@Composable
fun PagingAnimeItemsLazyVerticalGrid(
    anime: LazyPagingItems<UiAnimeItem>
) {
    // Paging grid — items loaded on-demand
    AnimeLazyVerticalGridContainer {
        items(
            count = anime.itemCount,
            key = { it }
        ) { index ->
            val item = anime[index]

            // Show card only if item actually loaded
            item?.let {
                AnimeCard(
                    posterPath = it.poster.fullPath(PosterType.PREVIEW),
                    genresString = it.genresAsString(),
                    title = it.name.russian,
                    onCardClick = {} // navigation can be added later
                )
            }
        }
    }
}

@Composable
private fun AnimeLazyVerticalGridContainer(
    content: LazyGridScope.() -> Unit
) {
    // Shared grid container for both static and paging content
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(AnimeCardUtils.CARD_WIDTH.dp),
        verticalArrangement = Arrangement.spacedBy(AnimeLVGContainerUtils.ARRANGEMENT_ALIGNMENT_PADDING.dp),
        horizontalArrangement = Arrangement.spacedBy(AnimeLVGContainerUtils.ARRANGEMENT_ALIGNMENT_PADDING.dp),
        contentPadding = PaddingValues(AnimeLVGContainerUtils.ARRANGEMENT_ALIGNMENT_PADDING.dp),
        content = content
    )
}