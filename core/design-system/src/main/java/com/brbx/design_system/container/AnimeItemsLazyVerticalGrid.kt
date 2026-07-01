package com.brbx.design_system.container

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.brbx.design_system.common.DesignConstants
import com.brbx.design_system.component.anime_card.AnimeCard
import com.brbx.design_system.component.anime_card.AnimeCardConstants
import com.brbx.design_system.component.anime_card.AnimeCardModel
import com.brbx.design_system.component.anime_card.AnimeCardShimmer
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.bDimens

@Composable
fun AnimeItemsLazyVerticalGrid(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    columns: GridCells =
        GridCells.Adaptive(minSize = AnimeCardConstants.Width),
    contentPadding: PaddingValues =
        PaddingValues(all = bDimens.micro8 - DesignConstants.elevation),
    verticalArrangement: Arrangement.Vertical =
        Arrangement.spacedBy(bDimens.micro8 - DesignConstants.elevation),
    horizontalArrangement: Arrangement.Horizontal =
        Arrangement.spacedBy(bDimens.micro8 - DesignConstants.elevation),
    content: LazyGridScope.() -> Unit,
) =
    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        overscrollEffect = overscrollEffect,
        content = content,
    )

inline fun LazyGridScope.animeItems(
    items: LazyPagingItems<AnimeCardModel>,
    crossinline onItemClick: (id: Int) -> Unit,
    itemModifier: Modifier = Modifier,
) =
    items(
        count = items.itemCount,
        key = { index -> index },
    ) { index ->
        val current = items[index]
        current?.let { anime ->
            AnimeCard(
                model = anime,
                onClick = { onItemClick(current.id) },
                modifier = itemModifier.brbxAnimateItem(scope = this),
            )
        }
    }

fun LazyGridScope.animeItemsShimmer(
    count: Int = 6,
    itemModifier: Modifier = Modifier,
) {
    items(
        count = count,
        key = { index -> index },
    ) {
        AnimeCardShimmer(
            modifier = itemModifier.brbxAnimateItem(scope = this)
        )
    }
}