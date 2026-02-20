@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.collections.screen

import androidx.activity.compose.BackHandler
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
import com.example.data.models.auth.UserAuthState
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.common.anime_item.AnimeItem
import com.example.data.models.theme.TabType
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.sections.LoggedOutSection
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.theme.logic.ThemeState
import com.example.design_system.theme.theme.mColors
import com.example.design_system.utils.CommonStrings
import kotlinx.coroutines.launch

private val TopBarLabelRes = R.string.collections_top_bar_label

/**
 * The primary UI layout for the Collections screen.
 * * **Scaffold:** Integrates a [SearchingTopBar] with nested scroll behavior.
 * * **Auth Management:** Conditionally displays the [AuthBS] (Bottom Sheet) based on the current [CollectionsState].
 * * **State Switching:** Toggles between [LoggedInContent] and [LoggedOutSection] based on [UserAuthState].
 *
 * @param state The current UI state containing auth, search, and collection data.
 * @param themeState Current theme configuration for visual styling.
 * @param pagingItemsMap Map providing the [LazyPagingItems] for each collection category.
 * @param snackbarHostState Manages the display of transient snackbar messages.
 * @param onIntent Lambda to dispatch [CollectionsIntent] to the ViewModel.
 * @param onEffect Lambda to trigger [UiEffect] side-effects (Navigation, Snackbars).
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
    BackHandler {
        if (state.filtersState.isSearching) {
            onIntent(CollectionsIntent.ToggleIsSearching)
        }
    }

    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            SearchingTopBar(
                enterAlways = true,
                text = stringResource(TopBarLabelRes),
                scrollBehavior = topBarScrollBehavior,
                isSearching = state.filtersState.isSearching,
                query = state.filtersState.requestParameters.search,
                onQueryChange = { onIntent(CollectionsIntent.UpdateQuery(it)) },
                onToggleSearch = { onIntent(CollectionsIntent.ToggleIsSearching) },
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
    ) { innerPadding ->

        // Auth Bottom Sheet
        if (state.authState.isAuthBSVisible) {
            AuthBS(
                login = state.authState.login,
                password = state.authState.password,
                incorrectEmailOrPassword = state.authState.isError,
                onDismissRequest = { onIntent(CollectionsIntent.ToggleIsAuthBSVisible) },
                onAuthClick = { onIntent(CollectionsIntent.GetTokens) },
                onPasswordChange = { onIntent(CollectionsIntent.UpdatePassword(it)) },
                onEmailChange = { onIntent(CollectionsIntent.UpdateLogin(it)) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(top = innerPadding.calculateTopPadding(), bottom = calculateNavBarSize())
        ) {
            when (state.authState.userAuthState) {
                UserAuthState.LoggedIn -> LoggedInContent(
                    selectedCollection = state.selectedCollection,
                    themeState = themeState,
                    pagingItemsMap = pagingItemsMap,
                    onIntent = onIntent,
                    onEffect = onEffect,
                    query = state.filtersState.requestParameters.search,
                    isSearching = state.filtersState.isSearching,
                )
                UserAuthState.LoggedOut -> LoggedOutSection(
                    onAuthClick = { onIntent(CollectionsIntent.ToggleIsAuthBSVisible) }
                )
            }
        }
    }
}

/**
 * Orchestrates the UI for authenticated users.
 * * **Load State Management:** Uses [PagingStatesContainer] to monitor the health of the
 * currently selected [LazyPagingItems], triggering error snackbars if loading fails.
 * * **Pager Navigation:** Displays the [CollectionsPagerContent].
 */
@Composable
private fun LoggedInContent(
    query: String,
    isSearching: Boolean,
    selectedCollection: Collection,
    themeState: ThemeState,
    pagingItemsMap: Map<Collection, LazyPagingItems<AnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Observe LoadState for current collection to handle errors (SnackBar)
    val currentItems = pagingItemsMap[selectedCollection]!!
    PagingStatesContainer(
        items = currentItems,
        onRetryRequest = { messageRes, retry ->
            onEffect(
                UiEffect.ShowSnackbarWithAction(
                    messageRes = messageRes.toInt(),
                    actionLabel = CommonStrings.RETRY,
                    action = retry
                )
            )
        }
    )

    CollectionsPagerContent(
        tabType = themeState.tabType,
        pagingItemsMap = pagingItemsMap,
        onIntent = onIntent,
        onEffect = onEffect,
        selectedCollection = selectedCollection,
        isSearching = isSearching,
        query = query,
    )
}

/**
 * Manages the horizontal paging and tab synchronization for anime collections.
 * * **Synchronization:** Uses a [LaunchedEffect] to update the [CollectionsVM] when the
 * user swipes between pages, ensuring the state and pager remain in sync.
 * * **Pager State:** Utilizes [rememberPagerState] to preserve scroll position across swipes.
 * * **Tabs:** Renders the [CollectionsTabRow] as the primary navigation anchor.
 */
@Composable
private fun CollectionsPagerContent(
    isSearching: Boolean,
    query: String,
    tabType: TabType,
    selectedCollection: Collection,
    pagingItemsMap: Map<Collection, LazyPagingItems<AnimeItem>>,
    onIntent: (CollectionsIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val pagerScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = selectedCollection.toIndex()
    ) { Collection.entries.size }

    Column {
        // Synchronize Pager state with ViewModel state
        LaunchedEffect(pagerState.currentPage) {
            onIntent(CollectionsIntent.SetCollection(pagerState.currentPage.toCollection()))
        }

        CollectionsTabRow(
            tabType = tabType,
            selectedCollection = selectedCollection,
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
                isSearching = isSearching,
                query = query
            )
        }
    }
}

// Helpers for Enum <-> Index conversion to keep code clean
// Assuming your Enum looks like this, extend these helpers:
internal fun Collection.toIndex(): Int = ordinal
internal fun Int.toCollection(): Collection = Collection.entries[this]