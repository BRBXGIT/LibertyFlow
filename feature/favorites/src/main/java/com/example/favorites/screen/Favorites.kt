@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.favorites.screen

import com.example.favorites.screen.FavoritesIntent.UpdateAuthForm.AuthField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.auth.AuthState
import com.example.data.models.common.ui_anime_item.AnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.components.sections.LoggedOutSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import com.example.design_system.theme.theme.mColors
import com.example.favorites.R

private val TopBarLabel = R.string.favorites_top_bar_label

@Composable
fun Favorites(
    state: FavoritesState,
    favorites: LazyPagingItems<AnimeItem>,
    snackbarHostState: SnackbarHostState,
    onIntent: (FavoritesIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            SearchingTopBar(
                isLoading = state.loadingState.isLoading,
                label = stringResource(TopBarLabel),
                scrollBehavior = scrollBehavior,
                searchForm = state.searchForm,
                onQueryChange = { onIntent(FavoritesIntent.UpdateQuery(it)) },
                onToggleSearch = { onIntent(FavoritesIntent.ToggleIsSearching) },
            )
        }
    ) { innerPadding ->
        if (state.authForm.isAuthBSVisible) {
            AuthBS(
                email = state.authForm.login,
                password = state.authForm.password,
                incorrectEmailOrPassword = state.authForm.isError,
                onDismissRequest = { onIntent(FavoritesIntent.ToggleIsAuthBSVisible) },
                onAuthClick = { onIntent(FavoritesIntent.GetTokens) },
                onPasswordChange = { onIntent(FavoritesIntent.UpdateAuthForm(AuthField.Password(it))) },
                onEmailChange = { onIntent(FavoritesIntent.UpdateAuthForm(AuthField.Email(it))) }
            )
        }

        // Pull-to-refresh container
        VibratingContainer(
            isRefreshing = state.loadingState.isLoading,
            onRefresh = favorites::refresh,
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = calculateNavBarSize()
                )
        ) {
            MainContent(
                authState = state.authState,
                isError = state.loadingState.isError,
                favorites = favorites,
                onIntent = onIntent,
                onEffect = onEffect
            )
        }
    }
}

@Composable
private fun MainContent(
    authState: AuthState,
    isError: Boolean,
    favorites: LazyPagingItems<AnimeItem>,
    onIntent: (FavoritesIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    when (authState) {
        AuthState.LoggedIn ->
            LoggedInContent(
                isError = isError,
                favorites = favorites,
                onIntent = onIntent,
                onEffect = onEffect
            )
        AuthState.LoggedOut ->
            LoggedOutSection(
                onAuthClick = {
                    onIntent(FavoritesIntent.ToggleIsAuthBSVisible)
                }
            )
    }
}

private const val RETRY = "Retry"

@Composable
private fun LoggedInContent(
    isError: Boolean,
    favorites: LazyPagingItems<AnimeItem>,
    onIntent: (FavoritesIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Sync paging state with UI flags
    PagingStatesContainer(
        items = favorites,
        onLoadingChange = { onIntent(FavoritesIntent.SetIsLoading(it)) },
        onErrorChange = { onIntent(FavoritesIntent.SetIsError(it)) },
        onRetryRequest = { messageRes, retry ->
            onEffect(
                UiEffect.ShowSnackbarWithAction(
                    messageRes = messageRes.toInt(),
                    actionLabel = RETRY,
                    action = retry
                )
            )
        }
    )

    if (isError) {
        ErrorSection()
        return
    }

    PagingAnimeItemsLazyVerticalGrid(
        anime = favorites,
        onItemClick = {
            onEffect(UiEffect.Navigate(AnimeDetailsRoute(it)))
        }
    )
}
