@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.collections.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.collections.R
import com.example.collections.components.CollectionPage
import com.example.collections.components.CollectionsPager
import com.example.collections.components.CollectionsTabRow
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.UiEffect
import com.example.data.models.auth.AuthState
import com.example.data.models.common.mappers.toIndex
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
fun Collections(
    collectionsState: CollectionsState,
    collections: List<LazyPagingItems<UiAnimeItem>>,
    snackbarHostState: SnackbarHostState,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Pinned TopAppBar behavior
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            // Searchable top bar
            SearchingTopBar(
                showIndicator = false,
                label = stringResource(TopBarLabel),
                scrollBehavior = topBarScrollBehavior,
                query = collectionsState.query,
                onQueryChange = { onIntent(CollectionsIntent.UpdateQuery(it)) },
                isSearching = collectionsState.isSearching,
                onSearchChange = { onIntent(CollectionsIntent.ToggleIsSearching) },
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        // Authentication BottomSheet
        if (collectionsState.isAuthBSVisible) {
            AuthBS(
                email = collectionsState.email,
                password = collectionsState.password,
                incorrectEmailOrPassword = collectionsState.isPasswordOrEmailIncorrect,
                onDismissRequest = { onIntent(CollectionsIntent.ToggleIsAuthBSVisible) },
                onAuthClick = { onIntent(CollectionsIntent.GetTokens) },
                onPasswordChange = { onIntent(CollectionsIntent.UpdatePassword(it)) },
                onEmailChange = { onIntent(CollectionsIntent.UpdateEmail(it)) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = calculateNavBarSize()
                )
        ) {
            // Switch content based on auth state
            when (collectionsState.authState) {
                AuthState.LoggedIn -> LoggedInContent(
                    collectionsState = collectionsState,
                    collections = collections,
                    onIntent = onIntent,
                    onEffect = onEffect
                )

                AuthState.LoggedOut -> LoggedOutSection(
                    onAuthClick = {
                        onIntent(CollectionsIntent.ToggleIsAuthBSVisible)
                    }
                )
            }
        }
    }
}

@Composable
private fun LoggedInContent(
    collectionsState: CollectionsState,
    collections: List<LazyPagingItems<UiAnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Observe paging states for every collection
    collections.forEach { collection ->
        PagingStatesContainer(
            items = collection,
            onErrorChange = { onIntent(CollectionsIntent.SetIsError(it)) },
            onRetryRequest = { messageRes, retry ->
                onEffect(
                    UiEffect.ShowSnackbar(
                        messageRes = messageRes.toInt(),
                        actionLabel = "Retry",
                        action = retry
                    )
                )
            }
        )
    }

    CollectionsContent(
        collectionsState = collectionsState,
        collections = collections,
        onIntent = onIntent,
        onEffect = onEffect
    )
}

@Composable
private fun CollectionsContent(
    collectionsState: CollectionsState,
    collections: List<LazyPagingItems<UiAnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val pagerState = rememberPagerState { Collection.entries.size }
    val pagerScope = rememberCoroutineScope()

    Column {
        // Tabs for collections
        CollectionsTabRow(
            selectedCollection = collectionsState.selectedCollection,
            onTabClick = { collection ->
                onIntent(CollectionsIntent.SetCollection(collection))
                pagerScope.launch {
                    pagerState.animateScrollToPage(collection.toIndex())
                }
            }
        )

        val isLoading = collections[pagerState.currentPage].loadState.refresh is LoadState.Loading

        // Pull-to-refresh wrapper
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { collections[pagerState.currentPage].refresh() }
        ) {
            // Horizontal pager with collections
            CollectionsPager(
                state = pagerState,
                onIntent = onIntent
            ) { pageIndex ->
                CollectionPage(
                    isError = collectionsState.isError,
                    isLoading = isLoading,
                    collection = collections[pageIndex],
                    onItemClick = { onEffect(UiEffect.Navigate(AnimeDetailsRoute(it))) }
                )
            }
        }
    }
}