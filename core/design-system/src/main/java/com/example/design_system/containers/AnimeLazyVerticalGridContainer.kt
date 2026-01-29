package com.example.design_system.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.data.models.common.common.PosterType
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.design_system.components.cards.AnimeCard
import com.example.design_system.components.cards.AnimeCardUtils


private val ArrangementAlignmentPadding = 16.dp

@Composable
fun PagingAnimeItemsLazyVerticalGrid(
    onItemClick: (Int) -> Unit,
    anime: LazyPagingItems<AnimeItem>,
    extraContent: LazyGridScope.() -> Unit = {}
) {
    // Paging grid — items loaded on-demand
    AnimeLazyVerticalGridContainer {
        extraContent(this)

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
                    onCardClick = { onItemClick(it.id) }
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
        verticalArrangement = Arrangement.spacedBy(ArrangementAlignmentPadding),
        horizontalArrangement = Arrangement.spacedBy(ArrangementAlignmentPadding),
        contentPadding = PaddingValues(ArrangementAlignmentPadding),
        content = content
    )
}