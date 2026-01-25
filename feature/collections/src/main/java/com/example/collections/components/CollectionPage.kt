@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.collections.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid

private const val ZERO_ELEMENTS = 0
/**
 * Displays a single collection page.
 *
 * This function is marked as internal because it is used
 * only within the collections feature module.
 */
@Composable
internal fun CollectionPage(
    query: String,
    items: LazyPagingItems<AnimeItem>,
    onItemClick: (Int) -> Unit,
) {
    val loadState = items.loadState.refresh

    val isLoading = loadState is LoadState.Loading
    val isError = loadState is LoadState.Error

    val isEmptyWithQuery = loadState is LoadState.NotLoading
            && items.itemCount == ZERO_ELEMENTS
                && query.isNotEmpty()
    val isEmptyWithoutQuery = loadState is LoadState.NotLoading
            && items.itemCount == ZERO_ELEMENTS
                && query.isEmpty()

    Box(Modifier.fillMaxSize()) {
        when {
            isError -> ErrorSection()

            isEmptyWithQuery -> EmptyCollection(false)

            isEmptyWithoutQuery -> EmptyCollection(true)

            else -> {
                if (isLoading && items.itemCount == ZERO_ELEMENTS) {
                    ContainedLoadingIndicator(Modifier.align(Alignment.Center))
                } else {
                    PagingAnimeItemsLazyVerticalGrid(
                        anime = items,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}