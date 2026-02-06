@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.collections.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid

private const val ZERO_ELEMENTS = 0

/**
 * Displays a single collection page with handled states for loading, errors, and empty results.
 * * Internal visibility restricts usage to the collections feature module.
 */
@Composable
internal fun CollectionPage(
    query: String,
    items: LazyPagingItems<AnimeItem>,
    onItemClick: (Int) -> Unit,
) {
    // Wrap state calculation in remember/derivedStateOf to optimize recomposition performance.
    // This ensures pageState only updates when essential properties change.
    val pageState by remember(items.loadState.refresh, items.itemCount, query) {
        derivedStateOf {
            calculatePageState(items.loadState.refresh, items.itemCount, query)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (pageState) {
            PageState.Error -> ErrorSection()

            PageState.EmptyWithQuery -> EmptyCollection(emptyQuery = false)

            PageState.EmptyWithoutQuery -> EmptyCollection(emptyQuery = true)

            PageState.Loading -> {
                ContainedLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }

            PageState.Ready -> {
                PagingAnimeItemsLazyVerticalGrid(
                    anime = items,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

/**
 * Possible UI states for the Collection Screen.
 */
private enum class PageState {
    Error, EmptyWithQuery, EmptyWithoutQuery, Loading, Ready
}

/**
 * Maps the Paging load state and item count to a high-level [PageState].
 * * @param loadState The current refresh state of the Paging data.
 * @param itemCount Total items currently loaded.
 * @param query The current search/filter string.
 */
private fun calculatePageState(
    loadState: LoadState,
    itemCount: Int,
    query: String
): PageState {
    val isEmpty = itemCount == ZERO_ELEMENTS

    return when (loadState) {
        is LoadState.Error -> PageState.Error
        is LoadState.Loading -> {
            // Only show full-screen loading if the list is actually empty.
            if (isEmpty) PageState.Loading else PageState.Ready
        }
        is LoadState.NotLoading -> {
            if (isEmpty) {
                if (query.isNotEmpty()) PageState.EmptyWithQuery else PageState.EmptyWithoutQuery
            } else {
                PageState.Ready
            }
        }
    }
}