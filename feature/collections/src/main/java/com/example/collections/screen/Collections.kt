@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.collections.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.collections.R
import com.example.collections.components.CollectionPage
import com.example.collections.components.CollectionsTabRow
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.auth.AuthState
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.sections.LoggedOutSection
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.theme.mColors
import kotlinx.coroutines.launch

private val TopBarLabel = R.string.collections_top_bar_label

@Composable
internal fun Collections(
    state: CollectionsState,
    pagingItemsMap: Map<Collection, LazyPagingItems<UiAnimeItem>>,
    snackbarHostState: SnackbarHostState,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            SearchingTopBar(
                showIndicator = false,
                label = stringResource(TopBarLabel),
                scrollBehavior = topBarScrollBehavior,
                query = state.query,
                onQueryChange = { onIntent(CollectionsIntent.UpdateQuery(it)) },
                isSearching = state.isSearching,
                onSearchChange = { onIntent(CollectionsIntent.ToggleIsSearching) },
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
    ) { innerPadding ->

        // Auth Bottom Sheet
        if (state.isAuthBSVisible) {
            AuthBS(
                email = state.authForm.email,
                password = state.authForm.password,
                incorrectEmailOrPassword = state.authForm.isError,
                onDismissRequest = { onIntent(CollectionsIntent.ToggleIsAuthBSVisible) },
                onAuthClick = { onIntent(CollectionsIntent.GetTokens) },
                onPasswordChange = { onIntent(CollectionsIntent.UpdateAuthForm(CollectionsIntent.UpdateAuthForm.AuthField.Password(it))) },
                onEmailChange = { onIntent(CollectionsIntent.UpdateAuthForm(CollectionsIntent.UpdateAuthForm.AuthField.Email(it))) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(top = innerPadding.calculateTopPadding(), bottom = calculateNavBarSize())
        ) {
            when (state.authState) {
                AuthState.LoggedIn -> LoggedInContent(
                    state = state,
                    pagingItemsMap = pagingItemsMap,
                    onIntent = onIntent,
                    onEffect = onEffect
                )
                AuthState.LoggedOut -> LoggedOutSection(
                    onAuthClick = { onIntent(CollectionsIntent.ToggleIsAuthBSVisible) }
                )
            }
        }
    }
}

@Composable
private fun LoggedInContent(
    state: CollectionsState,
    pagingItemsMap: Map<Collection, LazyPagingItems<UiAnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Observe LoadState for all collections to handle global errors (SnackBar)
    pagingItemsMap.values.forEach { pagingItems ->
        PagingStatesContainer(
            items = pagingItems,
            onErrorChange = { onIntent(CollectionsIntent.SetIsError(it)) },
            onRetryRequest = { messageRes, retry ->
                onEffect(UiEffect.ShowSnackbar(messageRes.toInt(), "Retry", retry))
            }
        )
    }

    CollectionsPagerContent(
        state = state,
        pagingItemsMap = pagingItemsMap,
        onIntent = onIntent,
        onEffect = onEffect
    )
}

@Composable
private fun CollectionsPagerContent(
    state: CollectionsState,
    pagingItemsMap: Map<Collection, LazyPagingItems<UiAnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val pagerScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = state.selectedCollection.toIndex()
    ) { Collection.entries.size }

    Column {
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                onIntent(CollectionsIntent.SetCollection(page.toCollection()))
            }
        }

        CollectionsTabRow(
            selectedCollection = state.selectedCollection,
            onTabClick = { collection ->
                onIntent(CollectionsIntent.SetCollection(collection))
                pagerScope.launch { pagerState.animateScrollToPage(collection.toIndex()) }
            }
        )

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            key = { index -> index.toCollection() }
        ) { pageIndex ->
            val collectionEnum = pageIndex.toCollection()
            val pagingItems = pagingItemsMap.getValue(collectionEnum)

            CollectionPage(
                items = pagingItems,
                onItemClick = { onEffect(UiEffect.Navigate(AnimeDetailsRoute(it))) },
                query = state.query
            )
        }
    }
}

// Helpers for Enum <-> Index conversion to keep code clean
// Assuming your Enum looks like this, extend these helpers:
internal fun Collection.toIndex(): Int = ordinal
internal fun Int.toCollection(): Collection = Collection.entries[this]