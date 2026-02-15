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
import com.example.data.models.common.anime_item.AnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.sections.LoggedOutSection
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.theme.logic.ThemeState
import com.example.design_system.theme.theme.mColors
import kotlinx.coroutines.launch

private val TopBarLabel = R.string.collections_top_bar_label

private const val RETRY = "Retry"

/**
 * Main entry point for the Collections screen.
 * Handles the scaffold, top bar, and high-level auth state switching.
 */
@Composable
internal fun Collections(
    state: CollectionsState,
    themeState: ThemeState,
    pagingItemsMap: Map<Collection, LazyPagingItems<AnimeItem>>,
    snackbarHostState: SnackbarHostState,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            SearchingTopBar(
                enterAlways = true,
                text = stringResource(TopBarLabel),
                scrollBehavior = topBarScrollBehavior,
                searchForm = state.searchForm,
                onQueryChange = { onIntent(CollectionsIntent.UpdateQuery(it)) },
                onToggleSearch = { onIntent(CollectionsIntent.ToggleIsSearching) },
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
    ) { innerPadding ->

        // Auth Bottom Sheet
        if (state.authForm.isAuthBSVisible) {
            AuthBS(
                login = state.authForm.login,
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
                    themeState = themeState,
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

/**
 * Encapsulates the logic for the logged-in user experience, including paging states.
 */
@Composable
private fun LoggedInContent(
    state: CollectionsState,
    themeState: ThemeState,
    pagingItemsMap: Map<Collection, LazyPagingItems<AnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Observe LoadState for current collection to handle errors (SnackBar)
    val currentItems = pagingItemsMap[state.selectedCollection]!!
    PagingStatesContainer(
        items = currentItems,
        onErrorChange = { onIntent(CollectionsIntent.SetIsError(it)) },
        onRetryRequest = { messageRes, retry ->
            onEffect(UiEffect.ShowSnackbarWithAction(messageRes.toInt(), RETRY, retry))
        }
    )

    CollectionsPagerContent(
        themeState = themeState,
        state = state,
        pagingItemsMap = pagingItemsMap,
        onIntent = onIntent,
        onEffect = onEffect
    )
}

@Composable
private fun CollectionsPagerContent(
    themeState: ThemeState,
    state: CollectionsState,
    pagingItemsMap: Map<Collection, LazyPagingItems<AnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val pagerScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = state.selectedCollection.toIndex()
    ) { Collection.entries.size }

    Column {
        // Synchronize Pager state with ViewModel state
        LaunchedEffect(pagerState.currentPage) {
            onIntent(CollectionsIntent.SetCollection(pagerState.currentPage.toCollection()))
        }

        CollectionsTabRow(
            themeState = themeState,
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
                state = state
            )
        }
    }
}

// Helpers for Enum <-> Index conversion to keep code clean
// Assuming your Enum looks like this, extend these helpers:
internal fun Collection.toIndex(): Int = ordinal
internal fun Int.toCollection(): Collection = Collection.entries[this]