@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.favorites.screen

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
import com.example.data.models.auth.AuthState
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.components.sections.LoggedOutSection
import com.example.design_system.components.snackbars.SnackbarState
import com.example.design_system.components.snackbars.getSnackbarState
import com.example.design_system.components.snackbars.sendRetrySnackbar
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import com.example.favorites.R
import com.example.favorites.screen.FavoritesConstants.TopBarLabel
import kotlinx.coroutines.launch

// TODO FIX BUG WITH LABEL
private object FavoritesConstants {
    val TopBarLabel = R.string.top_bar_label
}

@Composable
fun Favorites(
    favoritesState: FavoritesState,
    favorites: LazyPagingItems<UiAnimeItem>,
    onIntent: (FavoritesIntent) -> Unit
) {
    val snackbars = getSnackbarState()

    val topBarScrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbars.snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        modifier = Modifier.fillMaxSize().nestedScroll(topBarScrollBehaviour.nestedScrollConnection),
        topBar = {
            SearchingTopBar(
                isLoading = favoritesState.isLoading,
                label = stringResource(TopBarLabel),
                scrollBehavior = topBarScrollBehaviour,
                query = favoritesState.query,
                onQueryChange = { onIntent(FavoritesIntent.UpdateQuery(it)) },
                isSearching = favoritesState.isSearching,
                onSearchChange = { onIntent(FavoritesIntent.ToggleIsSearching) },
            )
        }
    ) { innerPadding ->
        if (favoritesState.isAuthBSVisible) {
            AuthBS(
                email = favoritesState.email,
                password = favoritesState.password,
                incorrectEmailOrPassword = favoritesState.isPasswordOrEmailIncorrect,
                onDismissRequest = { onIntent(FavoritesIntent.ToggleIsAuthBSVisible) },
                onAuthClick = { onIntent(FavoritesIntent.GetTokens) },
                onPasswordChange = { onIntent(FavoritesIntent.UpdatePassword(it)) },
                onEmailChange = { onIntent(FavoritesIntent.UpdateEmail(it)) }
            )
        }

        VibratingContainer(
            topPadding = innerPadding.calculateTopPadding(),
            isRefreshing = favoritesState.isLoading,
            onRefresh = { favorites.refresh() }
        ) {
            when(favoritesState.authState) {
                AuthState.LoggedIn -> LoggedInContent(favoritesState, favorites, snackbars, onIntent)
                AuthState.LoggedOut -> LoggedOutSection(onAuthClick = { onIntent(FavoritesIntent.ToggleIsAuthBSVisible) })
            }
        }
    }
}

// Main content
@Composable
private fun LoggedInContent(
    favoritesState: FavoritesState,
    favorites: LazyPagingItems<UiAnimeItem>,
    snackbars: SnackbarState,
    onIntent: (FavoritesIntent) -> Unit
) {
    PagingStatesContainer(
        items = favorites,
        onLoadingChange = { onIntent(FavoritesIntent.SetIsLoading(it)) },
        onErrorChange = { onIntent(FavoritesIntent.SetIsError(it)) },
        onRetryRequest = { message, retry ->
            snackbars.snackbarScope.launch { sendRetrySnackbar(message, retry) }
        }
    )

    when(favoritesState.isError) {
        true -> ErrorSection()
        false -> PagingAnimeItemsLazyVerticalGrid(favorites)
    }
}