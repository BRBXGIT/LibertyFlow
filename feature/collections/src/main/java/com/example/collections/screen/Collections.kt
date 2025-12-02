@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.collections.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.collections.R
import com.example.collections.components.CollectionsTabRow
import com.example.collections.screen.CollectionsConstants.TopBarLabel
import com.example.data.models.auth.AuthState
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.components.sections.LoggedOutSection
import com.example.design_system.components.snackbars.SnackbarState
import com.example.design_system.components.snackbars.getSnackbarState
import com.example.design_system.components.snackbars.sendRetrySnackbar
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import kotlinx.coroutines.launch

// TODO FIX BUG WITH LABEL
private object CollectionsConstants {
    val TopBarLabel = R.string.top_bar_label
}

@Composable
fun Collections(
    collectionsState: CollectionsState,
    collectionAnime: LazyPagingItems<UiAnimeItem>,
    onIntent: (CollectionsIntent) -> Unit
) {
    val snackbars = getSnackbarState()

    val topBarScrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbars.snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        modifier = Modifier.fillMaxSize().nestedScroll(topBarScrollBehaviour.nestedScrollConnection),
        topBar = {
            SearchingTopBar(
                isLoading = collectionsState.isLoading,
                label = stringResource(TopBarLabel),
                scrollBehavior = topBarScrollBehaviour,
                query = collectionsState.query,
                onQueryChange = { onIntent(CollectionsIntent.UpdateQuery(it)) },
                isSearching = collectionsState.isSearching,
                onSearchChange = { onIntent(CollectionsIntent.ToggleIsSearching) },
            )
        }
    ) { innerPadding ->
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

        VibratingContainer(
            topPadding = innerPadding.calculateTopPadding(),
            isRefreshing = collectionsState.isLoading,
            onRefresh = { collectionAnime.refresh() }
        ) {
            when(collectionsState.authState) {
                AuthState.LoggedIn -> LoggedInContent(collectionsState, collectionAnime, snackbars, onIntent)
                AuthState.LoggedOut -> LoggedOutSection(onAuthClick = { onIntent(CollectionsIntent.ToggleIsAuthBSVisible) })
            }
        }
    }
}

// Main content
@Composable
private fun LoggedInContent(
    collectionsState: CollectionsState,
    collectionAnime: LazyPagingItems<UiAnimeItem>,
    snackbars: SnackbarState,
    onIntent: (CollectionsIntent) -> Unit
) {
    PagingStatesContainer(
        items = collectionAnime,
        onLoadingChange = { onIntent(CollectionsIntent.SetIsLoading(it)) },
        onErrorChange = { onIntent(CollectionsIntent.SetIsError(it)) },
        onRetryRequest = { message, retry ->
            snackbars.snackbarScope.launch { sendRetrySnackbar(message, retry) }
        }
    )

    when(collectionsState.isError) {
        true -> ErrorSection()
        false -> {
            Column {
                CollectionsTabRow(collectionsState.selectedCollection, onIntent)

                PagingAnimeItemsLazyVerticalGrid(collectionAnime)
            }
        }
    }
}